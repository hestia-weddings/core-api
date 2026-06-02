# Payment Gateway — Asaas Integration

## Overview

Héstia uses a **single centralized Asaas account** to process gift payments and transfer funds to couples. Guests pay via **PIX** or **Credit Card** on a secure, PCI-DSS compliant page hosted by Asaas. Payment confirmation arrives via webhook, crediting the couple's wallet. Couples can then withdraw their balance via PIX transfer.

## How It Works

### Checkout Flow (Guest → Wallet)

```
1. Guest picks a gift and clicks "Pay"
2. Frontend sends POST /w/{slug}/gift/{giftId}/checkout
3. Backend creates an Order (PENDING) and an Asaas Checkout session
4. Guest is redirected to the Asaas payment page
5. Guest pays (PIX or Credit Card)
6. Asaas sends webhook (CHECKOUT_PAID) to our backend
7. Backend marks Order as PAID and credits wallet balance
8. Gift stock is automatically decremented (via DB view)
```

### Transfer Flow (Wallet → Couple)

```
1. Couple requests withdrawal via POST /transfers
2. Backend validates available balance
3. Backend calls Asaas Transfer API (PIX to couple's key)
4. Transaction created as PENDING
5. Asaas sends webhook (TRANSFER_DONE or TRANSFER_FAILED)
6. On COMPLETED: deducts (amount + fee) from wallet balance
7. On FAILED: marks transaction failed, balance untouched
```

## Configuration

### App-level (single Asaas account)

All payments go through a single Héstia Asaas account. Configured via environment variables:

| Variable | Description |
|----------|-------------|
| `ASAAS_API_KEY` | Héstia's Asaas API key |
| `ASAAS_ENVIRONMENT` | `SANDBOX` or `PRODUCTION` |
| `ASAAS_WEBHOOK_TOKEN` | Global webhook validation token |

### Couple Setup (Wallet)

Each couple only needs to configure their **PIX key** for receiving transfers:

```
POST /api/v2/wallet?wedding={weddingId}
Authorization: Bearer {couple-token}

{
  "pix_key": "couple@email.com"
}
```

### Webhook Configuration in Asaas

In the Asaas dashboard: **Integrations → Webhooks → New Webhook**

| Setting | Value |
|---------|-------|
| URL | `https://your-domain.com/api/v2/webhook/asaas/{ASAAS_WEBHOOK_TOKEN}` |
| Events | `CHECKOUT_PAID`, `CHECKOUT_EXPIRED`, `CHECKOUT_CANCELED`, `TRANSFER_DONE`, `TRANSFER_FAILED` |
| Send type | `SEQUENTIALLY` |

## API Endpoints

### Guest (Public — no auth)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/w/{slug}/gift/{giftId}/checkout` | Create a checkout session |

**Request:**
```json
{
  "guest_name": "João Silva",
  "guest_email": "joao@email.com"
}
```

**Response:**
```json
{
  "checkout_url": "https://sandbox.asaas.com/checkoutSession/show/abc-123..."
}
```

### Wallet (Authenticated — Couple/Admin)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v2/wallet` | List wallets (paginated) |
| GET | `/api/v2/wallet/{id}` | Get wallet details |
| POST | `/api/v2/wallet` | Create wallet (one per wedding) |
| PATCH | `/api/v2/wallet/{id}` | Update wallet (PIX key) |

### Transfers (Authenticated — Couple/Admin)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v2/transfers` | Request a withdrawal |

**Request:**
```json
{
  "amount": 5000
}
```

> Amount is in **cents** and represents what the couple receives. Fee is deducted internally from the gross wallet balance.

**Response:**
```json
{
  "id": "uuid",
  "amount": 5000,
  "status": "PENDING",
  "created_at": "..."
}
```

### Orders (Authenticated — Couple/Admin)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v2/order` | List orders (paginated) |
| GET | `/api/v2/order/{id}` | Get order details |

### Webhook (Public — validated by token)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/webhook/asaas/{webhookToken}` | Receives Asaas events |

## Order Statuses

| Status | Meaning |
|--------|---------|
| `PENDING` | Checkout created, waiting for payment |
| `PAID` | Payment confirmed via webhook |
| `EXPIRED` | Checkout session expired |
| `FAILED` | Checkout was canceled |

## Transaction Statuses

| Status | Meaning |
|--------|---------|
| `PENDING` | Transfer requested, awaiting Asaas confirmation |
| `COMPLETED` | PIX transfer sent to couple |
| `FAILED` | Transfer failed, balance untouched |

## Fee Model

- Fee is configured per wallet in **basis points** (default: 500 = 5%)
- Fee is **invisible to the couple** — they see only `availableBalance` (max they can withdraw)
- Fee is deducted at **withdrawal time**, not at payment time
- Formula: `availableBalance = (wallet.balance - SUM(pending txns)) * 10000 / (10000 + fee)`

## Stock Management

Stock is managed automatically. The `gift_availability` database view calculates remaining stock:

```
remain = gift.stock - COUNT(orders WHERE status = 'PAID')
```

## Security

- **Centralized API key** — stored server-side in env vars, never exposed to frontend or couples
- **Webhook validation** — global secret token in URL path; only matching requests are processed
- **PCI-DSS compliance** — Héstia never handles card data; all payment input on Asaas hosted page
- **Idempotency** — duplicate webhook events ignored (status transitions happen exactly once)
- **Over-withdrawal prevention** — available balance accounts for pending transactions

## Architecture

```
src/main/java/com/hestia/api/
├── domain/
│   ├── payment/
│   │   ├── entity/Wallet.java, Transaction.java
│   │   ├── controller/WalletController.java, TransferController.java
│   │   ├── service/WalletService.java, TransferService.java
│   │   └── dto/
│   └── registry/
│       ├── entity/Order.java, Gift.java
│       └── controller/OrderController.java
├── guest/
│   ├── controller/GuestCheckoutController.java
│   ├── service/GuestCheckoutService.java
│   └── dto/CheckoutRequest.java, CheckoutResponse.java
└── infrastructure/
    └── asaas/
        ├── AsaasCheckoutClient.java
        ├── AsaasTransferClient.java
        ├── controller/AsaasWebhookController.java
        ├── service/AsaasWebhookService.java
        ├── dto/
        └── exception/
```

## Testing with Sandbox

1. Set `ASAAS_ENVIRONMENT=SANDBOX` and use a sandbox API key (`$aact_hmlg_*`)
2. Create a checkout — you'll get a sandbox URL
3. On the Asaas sandbox page, simulate PIX or use test card numbers
4. The webhook will fire to your configured URL

### Test Credit Cards (Sandbox)

See: https://docs.asaas.com/docs/testing-credit-card-payment
