# Test Suite Documentation

## Overview

Integration tests validating **authorization (RBAC)**, **multi-tenant isolation**, **payment lifecycle**, **transfer flow**, and **webhook handling** across all API endpoints. Tests run against an H2 in-memory database with PostgreSQL compatibility mode.

**158 tests** covering 3 roles × all endpoints × expected behaviors + Asaas client unit tests + checkout/webhook integration tests + end-to-end payment lifecycle.

## Run

```bash
make test           # Full suite (lint + tests + coverage) ~65s
make test-no-lint   # Tests only (faster)
```

## Results

```
Tests run: 158, Failures: 0, Errors: 0, Skipped: 0
Coverage — Instructions: 88%  |  Branches: 65%
```

## Architecture

```
src/test/java/com/hestia/api/
├── security/
│   ├── support/                          # Test infrastructure
│   │   ├── WithMockAuth.java            # Base annotation (configurable role)
│   │   ├── WithMockAdmin.java           # Preset: admin user (no weddingId)
│   │   ├── WithMockCouple.java          # Preset: couple user (weddingId = WEDDING_A)
│   │   └── MockSecurityContextFactory.java  # Builds AuthenticatedUser from annotations
│   └── access/                           # RBAC + tenant isolation tests
│       ├── AdminAccessTest.java          # Admin can access everything
│       ├── CoupleAccessTest.java         # Couple scoped to own wedding
│       └── GuestAccessTest.java          # Anonymous: public endpoints only
├── guest/
│   └── GuestCheckoutControllerTest.java  # Checkout flow (mocked Asaas client)
├── domain/payment/
│   └── TransferControllerTest.java       # Transfer + webhook lifecycle
├── payment/
│   └── PaymentLifecycleTest.java         # E2E: guest pays → wallet credited → couple withdraws
└── infrastructure/asaas/
    ├── AsaasCheckoutClientTest.java      # HTTP client unit tests (MockRestServiceServer)
    ├── AsaasTransferClientTest.java      # Transfer client unit tests
    └── AsaasWebhookControllerTest.java   # Webhook event handling
```

## Test Suites

| Suite | Tests | Description |
|-------|-------|-------------|
| Asaas Checkout Client | 5 | HTTP client: sandbox/prod URLs, error handling |
| Asaas Transfer Client | 2 | Transfer client: success + error handling |
| Asaas Webhook | 7 | Webhook events: PAID, EXPIRED, CANCELED, idempotency |
| Payment Lifecycle E2E | 2 | Full flow: checkout → payment → wallet credit → transfer → completion |
| Guest Checkout | 4 | Public checkout: success, 404s, validation |
| Transfer Controller | 5 | Transfer: create, insufficient balance, webhook done/failed, idempotency |
| Admin Access | 33 | Admin RBAC: full CRUD on all resources |
| Couple Access | 32 | Couple RBAC: own-wedding scope, tenant isolation |
| Guest (Anonymous) Access | 18 | Public endpoints only, 401 on protected |

## Test Database

- **Engine**: H2 with `MODE=PostgreSQL`
- **Schema**: Flyway migrations (`db/h2/migration/`)
- **Seed data**: `V4__test_data.sql` — 2 weddings, users, gifts, orders, wallet (balance=50000)
- **Isolation**: `@Transactional` rolls back after each test

### Seed Data IDs

| Entity | Wedding A | Wedding B |
|--------|-----------|-----------|
| Wedding | `11111111-1111-...-111111111111` | `22222222-2222-...-222222222222` |
| User (couple) | `bbbb0000-...-000000000001` | — |
| User (admin) | `aaaa0000-...-000000000001` | — |
| Invite | `cccc0000-...-000000000001` | `cccc0000-...-000000000002` |
| Guest | `dddd0000-...-000000000001` | `dddd0000-...-000000000002` |
| Gift | `eeee0000-...-000000000001` | `eeee0000-...-000000000002` |
| Message | `ffff0000-...-000000000001` | `ffff0000-...-000000000002` |
| Wallet | `11111111-1111-...-111111111111` | — |
| Order (PENDING) | `bbbb1111-...-000000000001` | — |

## Mock Security

Custom annotations replace Spring Security context with a fake `AuthenticatedUser`:

```java
@WithMockAdmin   // role=ADMIN, weddingId=null
@WithMockCouple  // role=COUPLE, weddingId=WEDDING_A
```

## Key Test Scenarios

### Payment Lifecycle E2E (`PaymentLifecycleTest`)

```
Guest checkout → Asaas CHECKOUT_PAID webhook → wallet balance credited
→ Couple sees availableBalance (fee-adjusted) → Couple requests transfer
→ Asaas TRANSFER_DONE webhook → transaction COMPLETED, balance deducted
```

Also tests: transfer failure keeps balance untouched.

### Transfer Controller (`TransferControllerTest`)

| Test | Verifies |
|------|----------|
| Creates transfer | Transaction PENDING, Asaas called |
| Insufficient balance | 422 returned |
| TRANSFER_DONE webhook | Transaction → COMPLETED, balance deducted |
| Idempotent webhook | Duplicate ignored, balance unchanged |
| TRANSFER_FAILED webhook | Transaction → FAILED, balance untouched |

### Asaas Webhook (`AsaasWebhookControllerTest`)

| Event | Result |
|-------|--------|
| `CHECKOUT_PAID` | Order → PAID, wallet balance credited |
| `CHECKOUT_EXPIRED` | Order → EXPIRED |
| `CHECKOUT_CANCELED` | Order → FAILED |
| Invalid token | 404 |
| Duplicate event | Idempotent (no state change) |
| Unknown event | 200, ignored |

## Coverage

JaCoCo generates reports at `target/site/jacoco/index.html` after `make test`.

## Adding New Tests

1. Use `@WithMockAdmin` or `@WithMockCouple` at class or method level
2. Add seed data to `V4__test_data.sql` if new entities are needed
3. Follow the pattern: `mockMvc.perform(verb("/path")).andExpect(status().isXxx())`
4. Group by domain using `@Nested` + `@DisplayName`
5. For external services (Asaas), use `@MockitoBean` to mock the client
6. Run `mvn spotless:apply` before committing
