# Test Suite Documentation

## Overview

Integration tests validating **authorization (RBAC)**, **multi-tenant isolation**, **payment gateway integration**, and **webhook handling** across all API endpoints. Tests run against an H2 in-memory database with PostgreSQL compatibility mode.

**149 tests** covering 3 roles × all endpoints × expected behaviors + Asaas client unit tests + checkout/webhook integration tests.

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
└── infrastructure/asaas/
    ├── AsaasCheckoutClientTest.java      # HTTP client unit tests (MockRestServiceServer)
    └── AsaasWebhookControllerTest.java   # Webhook event handling
```

## Test Database

- **Engine**: H2 with `MODE=PostgreSQL`
- **Schema**: Flyway migrations (`db/h2/migration/`)
- **Seed data**: `V4__test_data.sql` — 2 weddings, 2 users, sample entities, payment config, order
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
| Payment Config | `aaaa1111-...-000000000001` | — |
| Order (PENDING) | `bbbb1111-...-000000000001` | — |

## Mock Security

Custom annotations replace Spring Security context with a fake `AuthenticatedUser`:

```java
@WithMockAdmin   // role=ADMIN, weddingId=null
@WithMockCouple  // role=COUPLE, weddingId=WEDDING_A
```

## Test Suites

### Admin (`AdminAccessTest`)

| Domain | GET all | GET /{id} | POST | PATCH /{id} | DELETE /{id} |
|--------|---------|-----------|------|-------------|--------------|
| Wedding | ✅ 200 | ✅ 200 | ✅ 201 | ✅ 200 | ✅ 204 |
| Account | ✅ 200 | — | ✅ 201 | ✅ 200 | ✅ 204 |
| Invite | ✅ 200 | ✅ 200 | ✅ 201 | ✅ 200 | ✅ 204 |
| Guest | ✅ 200 | ✅ 200 | ✅ 201 | ✅ 200 | ✅ 204 |
| Gift | ✅ 200 | ✅ 200 | ✅ 201 | ✅ 200 | ✅ 204 |
| Message | ✅ 200 | ✅ 200 | — | ✅ 200 | ✅ 204 |
| Payment Config | ✅ 200 | ✅ 200 | ✅ 201 | ✅ 200 | — |
| Order | ✅ 200 | ✅ 200 | — | — | — |

### Couple (`CoupleAccessTest`)

| Domain | GET all | GET own | GET other | POST | PATCH own | PATCH other |
|--------|---------|---------|-----------|------|-----------|-------------|
| Invite | ✅ 200 | ✅ 200 | ✅ 404 | ✅ 201 | ✅ 200 | ✅ 404 |
| Guest | ✅ 200 | ✅ 200 | ✅ 404 | ✅ 201 | ✅ 200 | ✅ 404 |
| Gift | ✅ 200 | ✅ 200 | ✅ 404 | ✅ 201 | ✅ 200 | ✅ 404 |
| Message | ✅ 200 | ✅ 200 | ✅ 404 | — | ✅ 200 | ✅ 404 |
| Payment Config | ✅ 200 | ✅ 200 | ✅ 404 | ✅ 409 (dup) | ✅ 200 | ✅ 404 |
| Order | ✅ 200 | ✅ 200 | ✅ 404 | — | — | — |
| Wedding (own) | — | ✅ 200 | ✅ 403 | — | ✅ 200 | ✅ 403 |
| Admin-only | ✅ 403 | — | — | ✅ 403 | — | ✅ 403 |

### Guest/Anonymous (`GuestAccessTest`)

| Endpoint | Valid slug | Invalid slug |
|----------|-----------|--------------|
| `POST /w/{slug}/rsvp/invite/search` | ✅ 200 | ✅ 404 |
| `GET /w/{slug}/rsvp/guest` | ✅ 200 | ✅ 404 |
| `PATCH /w/{slug}/rsvp/guest/status/{id}` | ✅ 200 | ✅ 404 |
| `GET /w/{slug}/gift` | ✅ 200 | ✅ 404 |
| `GET /w/{slug}/gift/{id}` | ✅ 200 | ✅ 404 |
| `POST /w/{slug}/message` | ✅ 201 | ✅ 404 |
| All protected endpoints | ✅ 401 | — |

### Guest Checkout (`GuestCheckoutControllerTest`)

| Test | Expected |
|------|----------|
| Creates checkout successfully | ✅ 200 + `checkout_url` |
| Invalid slug | ✅ 404 |
| Nonexistent gift | ✅ 404 |
| Missing required fields | ✅ 400 |

### Asaas Checkout Client (`AsaasCheckoutClientTest`)

| Test | Expected |
|------|----------|
| Creates checkout (sandbox URL) | ✅ Response with id + link |
| Uses production URL | ✅ Correct base URL |
| Unauthorized (401) | ✅ Throws AsaasCheckoutException |
| Bad request (400) | ✅ Throws AsaasCheckoutException |
| Server error (500) | ✅ Throws AsaasCheckoutException |

### Asaas Webhook (`AsaasWebhookControllerTest`)

| Test | Expected |
|------|----------|
| CHECKOUT_PAID | ✅ 200, order → PAID |
| CHECKOUT_EXPIRED | ✅ 200, order → EXPIRED |
| CHECKOUT_CANCELED | ✅ 200, order → FAILED |
| Invalid token | ✅ 404 |
| Unknown payment ID | ✅ 404 |
| Duplicate event (idempotent) | ✅ 200, no state change |
| Unknown event type | ✅ 200, ignored |

## Running Tests

```bash
# Full suite with formatting, linting, coverage
make test

# Tests only — skip lint (faster, ~30s vs ~90s)
make test-no-lint

# Single test class
mvn test -DskipTests=false -Dcheckstyle.skip=true -Dspotbugs.skip=true -Dspotless.check.skip=true -Dtest="AsaasWebhookControllerTest" -pl .
```

## Coverage

JaCoCo generates coverage reports at `target/site/jacoco/index.html` after `make test`.

```
  == Coverage ==
    [COV] Instructions: 89%  |  Branches: 68%
```

## Adding New Tests

1. Use `@WithMockAdmin` or `@WithMockCouple` at class or method level
2. Add seed data to `V4__test_data.sql` if new entities are needed
3. Follow the pattern: `mockMvc.perform(verb("/path")).andExpect(status().isXxx())`
4. Group by domain using `@Nested` + `@DisplayName`
5. For external services (Asaas), use `@MockitoBean` to mock the client
6. Run `mvn spotless:apply` before committing
