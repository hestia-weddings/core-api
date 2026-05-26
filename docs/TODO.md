# TODO — Step 5: Payment Gateway (Asaas Checkout)

## Overview

Integrate Asaas Checkout to allow guests to pay for gifts via PIX or Credit Card.
Each couple configures their own Asaas credentials. Guests are redirected to Asaas's
hosted checkout page (PCI-DSS compliant) and payment confirmation arrives via webhook.

## Flow

```
Guest (Frontend)          Héstia API              Asaas API
      |                        |                       |
      |-- POST /w/{slug}/gift/{giftId}/checkout ------>|
      |   {guestName, guestEmail}                      |
      |                        |-- Validate gift stock |
      |                        |-- Create Order(PENDING)
      |                        |-- POST /v3/checkouts ----------->|
      |                        |<-- {id: "session-id"} ----------|
      |                        |-- Save payment_id on Order      |
      |<-- {checkoutUrl} ------|                       |
      |                                                |
      |-- Guest pays on Asaas page ------------------->|
      |<-- Redirect to successUrl ---------------------|
      |                                                |
      |                        |<-- Webhook: PAYMENT_RECEIVED ---|
      |                        |-- Update Order → PAID |
      |                        |-- Decrement gift stock|
```

## Tasks

### Task 1: `payment_configs` table + entity

- [x] Flyway migration: `payment_configs` table
  - `id` (UUID, PK)
  - `api_key` (VARCHAR, NOT NULL) — Asaas access_token (format: `$aact_prod_*` or `$aact_hmlg_*`)
  - `environment` (VARCHAR, NOT NULL) — SANDBOX / PRODUCTION (determines base URL)
  - `webhook_token` (VARCHAR) — secret token to validate incoming webhooks
  - `wedding_id` (UUID, FK → weddings, UNIQUE)
  - `created_at`, `updated_at`, `is_active`
- [X] `PaymentConfig` entity in `domain/payment/`
- [X] `PaymentEnvironment` enum (SANDBOX, PRODUCTION)
- [X] `PaymentConfigRepository`

**Model rationale (based on Asaas docs):**
- `api_key`: The ONLY credential needed. Asaas identifies the account entirely by this key.
  No wallet ID, no account ID needed for checkout creation.
- `environment`: Determines the base URL:
  - SANDBOX → `https://api-sandbox.asaas.com/v3`
  - PRODUCTION → `https://api.asaas.com/v3`
- `webhook_token`: A secret we generate and share with the couple to validate webhook
  authenticity (Asaas doesn't sign webhooks — you validate by checking a custom header/token).
- `wedding_id` is UNIQUE: one payment config per wedding.
- No `wallet_id`: Only needed for split payments (not our use case).
- No `user_agent`: We use a fixed app name ("Hestia/1.0") as required by Asaas since 06/2024.

### Task 2: PaymentConfig CRUD (Couple-only)

- [X] `CreatePaymentConfigRequest` / `UpdatePaymentConfigRequest` DTOs
- [X] `PaymentConfigResponse` DTO
- [X] `PaymentConfigService` (create, update, get by wedding)
- [X] `PaymentConfigController` at `/api/v2/payment-config`
  - COUPLE role only, tenant-scoped
- [X] Integration tests (access control + CRUD)

### Task 3: `orders` table + entity

- [X] Flyway migration: `orders` table
  - `id` (UUID, PK)
  - `guest_name` (VARCHAR)
  - `guest_email` (VARCHAR)
  - `amount` (INTEGER — cents)
  - `status` (VARCHAR — PENDING / PAID / FAILED / EXPIRED)
  - `payment_id` (VARCHAR — Asaas checkout session ID)
  - `gift_id` (UUID, FK → gifts)
  - `wedding_id` (UUID, FK → weddings)
  - `created_at`, `updated_at`, `is_active`
- [X] `Order` entity, `OrderStatus` enum
- [X] `OrderRepository`

### Task 4: Asaas Checkout integration client

- [X] `AsaasCheckoutClient` (RestClient)
  - `POST {baseUrl}/v3/checkouts` (baseUrl from PaymentConfig.environment)
  - Headers: `access_token`, `Content-Type: application/json`, `User-Agent: Hestia/1.0`
  - Request body:
    - `billingTypes: ["PIX", "CREDIT_CARD"]`
    - `chargeTypes: ["DETACHED"]`
    - `minutesToExpire: 60` (configurable)
    - `externalReference: {orderId}` (links checkout to our Order)
    - `items: [{name, description, quantity: 1, value}]` (gift info)
    - `customerData: {name, email}` (from guest input)
    - `callback: {successUrl, cancelUrl, expiredUrl}`
  - Response: `{id, link, status}` — use `link` directly as checkout URL
- [X] `AsaasCheckoutRequest` / `AsaasCheckoutResponse` DTOs
- [X] Error handling (401 invalid key, 400 bad request, timeout)
- [X] Unit tests with mocked HTTP responses

### Task 5: Guest checkout endpoint (public)

- [ ] `POST /w/{slug}/gift/{giftId}/checkout`
  - Request: `{guestName, guestEmail}`
  - Response: `{checkoutUrl}`
- [ ] Logic:
  1. Validate gift exists & has stock (from `gift_availability` view)
  2. Load `PaymentConfig` for the wedding
  3. Create `Order` (PENDING)
  4. Call `AsaasCheckoutClient` → get session ID
  5. Save `payment_id` on Order
  6. Return checkout URL
- [ ] Edge cases: out of stock, no payment config, Asaas error (rollback)
- [ ] Integration tests (mocked Asaas client)

### Task 6: Webhook endpoint

- [ ] `POST /webhook/asaas/{webhookToken}` (public, validated by path token matching PaymentConfig)
- [ ] Parse webhook payload: extract `event` type + `checkout.id`
- [ ] Event handling:
  - `CHECKOUT_PAID` → find Order by `payment_id` (checkout ID) → update to PAID + decrement stock
  - `CHECKOUT_EXPIRED` → find Order by `payment_id` → update to EXPIRED
  - `CHECKOUT_CANCELED` → find Order by `payment_id` → update to FAILED
- [ ] Idempotency: ignore duplicate events for already-processed orders
- [ ] Resolve wedding from webhook token → scope the Order lookup
- [ ] Integration tests (simulated webhook payloads)

### Task 7: Order query endpoints (Couple + Admin)

- [ ] `GET /api/v2/order` — paginated, tenant-scoped
- [ ] `GET /api/v2/order/{id}` — single order detail
- [ ] `OrderResponse` DTO, `OrderMapper`, `OrderService`
- [ ] Access control: COUPLE sees own wedding, ADMIN with `wedding_id` param
- [ ] Integration tests

## Technical Decisions

| Decision | Choice | Rationale |
|----------|--------|-----------|
| Payment provider | Asaas Checkout | PCI-DSS hosted page, PIX+Card in one session, no card data handling |
| Credential storage | Per-wedding `payment_configs` table (UNIQUE) | Each couple owns their Asaas account (tax/legal reasons) |
| Credential fields | Only `api_key` + `environment` | Asaas identifies account entirely by API key — no wallet/account ID needed |
| Checkout type | `DETACHED` (one-time) | 98% of guests pay once |
| Billing types | `["PIX", "CREDIT_CARD"]` | Guest chooses at checkout |
| Customer data | Hybrid — name/email from Héstia, CPF on Asaas | Less friction, Asaas handles sensitive data |
| Payment confirmation | Webhook (`CHECKOUT_PAID` event) | Real-time, reliable, Asaas recommended approach |
| Stock decrement | On webhook (PAID), not on checkout creation | Avoids false decrements from abandoned checkouts |
| Order ↔ Checkout link | `externalReference` field in Asaas request | Asaas supports storing our Order ID in the checkout — enables correlation |
| Webhook validation | Custom `webhook_token` per wedding | Asaas doesn't sign payloads — we validate via a shared secret in header |
| User-Agent | Fixed `"Hestia/1.0"` | Required by Asaas for accounts created after 06/2024 |

## Asaas API Reference

- **Create Checkout**: `POST /v3/checkouts`
- **Auth header**: `access_token: {api_key}`
- **Required headers**: `Content-Type: application/json`, `User-Agent: Hestia/1.0`, `access_token: {key}`
- **Production URL**: `https://api.asaas.com/v3`
- **Sandbox URL**: `https://api-sandbox.asaas.com/v3`
- **Checkout URL**: Returned in response as `link` field (no need to build manually)
- **API Key format**: Production = `$aact_prod_*`, Sandbox = `$aact_hmlg_*`
- **`minutesToExpire`**: Min 10, max 1440 (24h)
- **`externalReference`**: Max 200 chars — use our Order UUID
- **Webhook events**: `CHECKOUT_CREATED`, `CHECKOUT_CANCELED`, `CHECKOUT_EXPIRED`, `CHECKOUT_PAID`
- **Docs**: https://docs.asaas.com/docs/introduction-1

### Checkout Response Schema

```json
{
  "id": "131ca662-56c8-4479-b5b3-fd61a413fce7",
  "link": "https://sandbox.asaas.com/checkoutSession/show/131ca662-...",
  "status": "ACTIVE",
  "billingTypes": ["CREDIT_CARD", "PIX"],
  "chargeTypes": ["DETACHED"],
  "minutesToExpire": 60,
  "externalReference": "our-order-uuid",
  "callback": { "successUrl": "...", "cancelUrl": "...", "expiredUrl": "..." },
  "items": []
}
```

### Webhook Payload (CHECKOUT_PAID)

```json
{
  "id": "evt_37260be8159d4472b4458d3de13efc2d&15370",
  "event": "CHECKOUT_PAID",
  "dateCreated": "2024-10-31 18:07:47",
  "checkout": {
    "id": "2bd251f0-09b2-44ff-8a0c-a5cb29e5bbda",
    "status": "PAID",
    "customer": "cus_000000018936",
    "items": []
  }
}
```
