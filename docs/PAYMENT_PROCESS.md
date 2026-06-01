# Payment Process — Full Test Tutorial

Step-by-step guide to test the complete payment flow locally (sandbox).

## Prerequisites

- App running (`make run`)
- Asaas sandbox account with API key (`$aact_hmlg_*`)
- `.env.dev` configured:
  ```properties
  ASAAS_API_KEY=$aact_hmlg_000YourKeyHere
  ASAAS_ENVIRONMENT=SANDBOX
  ASAAS_WEBHOOK_TOKEN=any-secret-token-you-choose
  ```

## Flow Overview

```
Guest pays gift → Order PENDING → Asaas Checkout
                                       ↓
                              Asaas webhook (PAID)
                                       ↓
                         Order PAID + Wallet balance credited
                                       ↓
                         Couple requests transfer → Transaction PENDING
                                       ↓
                              Asaas Transfer API (PIX)
                                       ↓
                         Asaas webhook (TRANSFER_DONE)
                                       ↓
                   Transaction COMPLETED + Balance deducted
```

---

## Step 1: Create a Wallet

The couple needs a wallet with their PIX key.

```http
POST /api/v2/wallet
Authorization: Bearer {couple-token}

{
  "pixKey": "couple@email.com"
}
```

Response:
```json
{
  "id": "uuid",
  "pixKey": "couple@email.com",
  "availableBalance": 0,
  "createdAt": "..."
}
```

---

## Step 2: Guest Pays a Gift (Checkout)

No auth required. Use the wedding slug.

```http
POST /w/{slug}/gift/{giftId}/checkout

{
  "guest_name": "João Silva",
  "guest_email": "joao@email.com"
}
```

Response:
```json
{
  "checkout_url": "https://sandbox.asaas.com/checkoutSession/show/abc-123"
}
```

Open `checkout_url` in a browser and complete the payment (use Asaas sandbox test cards or PIX simulation).

---

## Step 3: Simulate the Payment Webhook

If testing locally (no public URL), simulate the webhook manually:

```http
POST /webhook/asaas/{your-webhook-token}

{
  "id": "evt_001",
  "event": "CHECKOUT_PAID",
  "checkout": {
    "id": "the-asaas-payment-id",
    "status": "PAID"
  }
}
```

> The `checkout.id` must match the Asaas payment ID returned during checkout creation.

**What happens:**
- Order status → `PAID`
- Wallet balance += order amount (in cents)

---

## Step 4: Verify Wallet Balance

```http
GET /api/v2/wallet/{walletId}
Authorization: Bearer {couple-token}
```

Response:
```json
{
  "id": "uuid",
  "pixKey": "couple@email.com",
  "availableBalance": 9523,
  "createdAt": "..."
}
```

> `availableBalance` is the max the couple can withdraw (gross balance minus fee reservation).
> The couple never sees the fee — only the available amount.

---

## Step 5: Couple Requests a Transfer (Withdrawal)

```http
POST /api/v2/transfers
Authorization: Bearer {couple-token}

{
  "amount": 9523
}
```

`amount` = what the couple wants to **receive** (in cents).

Response:
```json
{
  "id": "uuid",
  "amount": 9523,
  "status": "PENDING",
  "createdAt": "..."
}
```

**What happens behind the scenes:**
- Transaction created (PENDING)
- Fee calculated: `amount * walletFee / 10000` (e.g., 9523 * 500 / 10000 = 476)
- Asaas Transfer API called → PIX sent to couple's pixKey
- Balance is NOT deducted yet (only on webhook confirmation)

---

## Step 6: Simulate the Transfer Webhook

```http
POST /webhook/asaas/{your-webhook-token}

{
  "id": "evt_002",
  "event": "TRANSFER_DONE",
  "transfer": {
    "id": "the-asaas-transfer-id"
  }
}
```

> The `transfer.id` must match the `asaas_transfer_id` stored in the transaction.

**What happens:**
- Transaction status → `COMPLETED`
- Wallet balance deducted by `amount + fee`

---

## Step 7: Verify Final State

```http
GET /api/v2/wallet/{walletId}
Authorization: Bearer {couple-token}
```

Balance should now be 0 (or reduced by the transfer amount + fee).

---

## Error Scenarios

### Insufficient balance

```http
POST /api/v2/transfers
{ "amount": 999999 }
```
→ `422 Unprocessable Entity`

### Transfer fails on Asaas

```http
POST /webhook/asaas/{your-webhook-token}

{
  "id": "evt_003",
  "event": "TRANSFER_FAILED",
  "transfer": { "id": "the-asaas-transfer-id" }
}
```
→ Transaction marked `FAILED`, balance untouched, couple can retry.

### Duplicate webhook (idempotency)

Sending the same `TRANSFER_DONE` webhook twice:
→ Second call is ignored. Balance is only deducted once.

---

## Webhook Configuration (Production)

In the Asaas dashboard: **Integrations → Webhooks → New Webhook**

| Setting | Value |
|---------|-------|
| URL | `https://your-domain.com/webhook/asaas/{ASAAS_WEBHOOK_TOKEN}` |
| Events | `CHECKOUT_PAID`, `CHECKOUT_EXPIRED`, `CHECKOUT_CANCELED`, `TRANSFER_DONE`, `TRANSFER_FAILED` |
| Send type | `SEQUENTIALLY` |

---

## Quick Reference

| Endpoint | Auth | Description |
|----------|------|-------------|
| `POST /w/{slug}/gift/{id}/checkout` | None | Guest starts payment |
| `POST /webhook/asaas/{token}` | None (token in path) | Asaas webhook |
| `GET /api/v2/wallet` | Couple/Admin | List wallets |
| `GET /api/v2/wallet/{id}` | Couple/Admin | Get wallet (shows availableBalance) |
| `POST /api/v2/wallet` | Couple/Admin | Create wallet |
| `PATCH /api/v2/wallet/{id}` | Couple/Admin | Update PIX key |
| `POST /api/v2/transfers` | Couple | Request withdrawal |
| `GET /api/v2/order` | Couple/Admin | List orders |
