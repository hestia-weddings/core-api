# Payment Gateway — Asaas Integration

## Overview

Héstia uses [Asaas Checkout](https://docs.asaas.com/docs/introduction-1) to process gift payments. Guests pay via **PIX** or **Credit Card** on a secure, PCI-DSS compliant page hosted by Asaas. Payment confirmation arrives via webhook.

## How It Works

```
1. Guest picks a gift and clicks "Pay"
2. Frontend sends POST /w/{slug}/gift/{giftId}/checkout
3. Backend creates an Order (PENDING) and an Asaas Checkout session
4. Guest is redirected to the Asaas payment page
5. Guest pays (PIX or Credit Card)
6. Asaas sends a webhook to our backend (CHECKOUT_PAID)
7. Backend marks the Order as PAID
8. Gift stock is automatically decremented (via DB view)
```

## Configuration (Couple Setup)

Each couple must create their own Asaas account and configure it in Héstia.

### 1. Create an Asaas Account

- Production: https://www.asaas.com
- Sandbox (testing): https://sandbox.asaas.com

### 2. Generate an API Key

In the Asaas dashboard: **Integrations → API Keys → Generate API Key**

- Production keys start with `$aact_prod_`
- Sandbox keys start with `$aact_hmlg_`

> ⚠️ The key is shown only once. Save it immediately.

### 3. Save Credentials in Héstia

```
POST /api/v2/payment-config
Authorization: Bearer {couple-token}

{
  "api_key": "$aact_hmlg_000YourKeyHere...",
  "environment": "SANDBOX"
}
```

The response will include an auto-generated `webhook_token`:

```json
{
  "id": "...",
  "api_key": "$aact_hmlg_000YourKeyHere...",
  "environment": "SANDBOX",
  "webhook_token": "a1b2c3d4e5f6..."
}
```

| Field | Description |
|-------|-------------|
| `api_key` | Your Asaas API key |
| `environment` | `SANDBOX` for testing, `PRODUCTION` for real payments |
| `webhook_token` | Auto-generated secret — use it in step 4 |

### 4. Configure Webhook in Asaas

In the Asaas dashboard: **Integrations → Webhooks → New Webhook**

| Setting | Value |
|---------|-------|
| URL | `https://your-domain.com/webhook/asaas/{webhook_token}` |
| Events | `CHECKOUT_PAID`, `CHECKOUT_EXPIRED`, `CHECKOUT_CANCELED` |
| Send type | `SEQUENTIALLY` |

Replace `{webhook_token}` with the value returned in step 3.

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

The frontend should redirect the guest to `checkout_url`.

### Webhook (Public — validated by token)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/webhook/asaas/{webhookToken}` | Receives Asaas payment events |

Handled automatically. No frontend action needed.

### Orders (Authenticated — Couple/Admin)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v2/order` | List orders (paginated) |
| GET | `/api/v2/order/{id}` | Get order details |

### Payment Config (Authenticated — Couple/Admin)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v2/payment-config` | List payment configs |
| GET | `/api/v2/payment-config/{id}` | Get config details |
| POST | `/api/v2/payment-config` | Create config (one per wedding) |
| PATCH | `/api/v2/payment-config/{id}` | Update config |

## Order Statuses

| Status | Meaning |
|--------|---------|
| `PENDING` | Checkout created, waiting for payment |
| `PAID` | Payment confirmed via webhook |
| `EXPIRED` | Checkout session expired (guest didn't pay in time) |
| `FAILED` | Checkout was canceled |

## Stock Management

Stock is managed automatically. The `gift_availability` database view calculates remaining stock by counting PAID orders:

```
remain = gift.stock - COUNT(orders WHERE status = 'PAID')
```

No manual stock decrement is needed. When an order becomes PAID, the gift's available stock decreases automatically.

## Testing with Sandbox

1. Set `environment` to `SANDBOX` in your payment config
2. Use a sandbox API key (`$aact_hmlg_*`)
3. Create a checkout — you'll get a sandbox URL
4. On the Asaas sandbox page, use test card numbers or simulate PIX payment
5. The webhook will fire to your configured URL

### Test Credit Cards (Sandbox)

See: https://docs.asaas.com/docs/testing-credit-card-payment

## Security

- **API keys** are stored server-side only — never exposed to the frontend
- **Webhook validation** uses a secret token in the URL path — only requests with the correct token are processed
- **PCI-DSS compliance** — Héstia never handles card data; all payment input happens on Asaas's hosted page
- **Idempotency** — duplicate webhook events are ignored (orders already processed won't change state)

## Architecture

```
src/main/java/com/hestia/api/
├── domain/
│   ├── payment/              # PaymentConfig entity, CRUD
│   └── registry/             # Order entity, OrderService, OrderController
├── guest/
│   ├── controller/GuestCheckoutController.java
│   ├── service/GuestCheckoutService.java
│   └── dto/CheckoutRequest.java, CheckoutResponse.java
└── infrastructure/
    └── asaas/
        ├── AsaasCheckoutClient.java        # HTTP client to Asaas API
        ├── controller/AsaasWebhookController.java
        ├── service/AsaasWebhookService.java
        ├── dto/AsaasCheckoutRequest.java
        ├── dto/AsaasCheckoutResponse.java
        ├── dto/AsaasWebhookPayload.java
        └── exception/AsaasCheckoutException.java
```
