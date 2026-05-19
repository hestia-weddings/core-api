# Test Suite Documentation

## Overview

Integration tests validating **authorization (RBAC)** and **multi-tenant isolation** across all API endpoints. Tests run against an H2 in-memory database with PostgreSQL compatibility mode.

**114 tests** covering 3 roles × all endpoints × expected behaviors.

## Architecture

```
src/test/java/com/hestia/api/security/
├── support/                          # Test infrastructure
│   ├── WithMockAuth.java            # Base annotation (configurable role)
│   ├── WithMockAdmin.java           # Preset: admin user (no weddingId)
│   ├── WithMockCouple.java          # Preset: couple user (weddingId = WEDDING_A)
│   └── MockSecurityContextFactory.java  # Builds AuthenticatedUser from annotations
└── access/                           # Test classes
    ├── AdminAccessTest.java          # Admin can access everything
    ├── CoupleAccessTest.java         # Couple scoped to own wedding
    └── GuestAccessTest.java          # Anonymous: public endpoints only
```

## Test Database

- **Engine**: H2 with `MODE=PostgreSQL`
- **Schema**: Flyway migrations (same as production)
- **Seed data**: `V4__test_data.sql` — 2 weddings, 2 users, sample entities
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

## Mock Security

Custom annotations replace Spring Security context with a fake `AuthenticatedUser`:

```java
@WithMockAdmin   // role=ADMIN, weddingId=null
@WithMockCouple  // role=COUPLE, weddingId=WEDDING_A
```

For custom scenarios:

```java
@WithMockAuth(
    userId = "...",
    authUserId = "...",
    email = "test@hestia.com",
    role = "COUPLE",
    weddingId = "11111111-1111-1111-1111-111111111111"
)
```

## Test Matrix

### Admin (`AdminAccessTest`)

| Domain | GET all | GET /{id} | POST | PATCH /{id} | DELETE /{id} |
|--------|---------|-----------|------|-------------|--------------|
| Wedding | ✅ 200 | ✅ 200 | ✅ 201 | ✅ 200 | ✅ 204 |
| Account | ✅ 200 | — | ✅ 201 | ✅ 200 | ✅ 204 |
| Invite | ✅ 200 | ✅ 200 | ✅ 201 (`?wedding=`) | ✅ 200 | ✅ 204 |
| Guest | ✅ 200 | ✅ 200 | ✅ 201 (`?wedding=`) | ✅ 200 | ✅ 204 |
| Gift | ✅ 200 | ✅ 200 | ✅ 201 (`?wedding=`) | ✅ 200 | ✅ 204 |
| Message | ✅ 200 | ✅ 200 | — | ✅ 200 | ✅ 204 |

- Admin has no `weddingId` — must pass `?wedding=` for POST (create)
- GET all without `?wedding=` returns all records (cross-tenant)
- GET/PATCH/DELETE by id: no wedding scoping (can access any resource)

### Couple (`CoupleAccessTest`)

| Domain | GET all | GET /{id} | POST | PATCH /{id} | DELETE /{id} |
|--------|---------|-----------|------|-------------|--------------|
| Invite (own) | ✅ 200 | ✅ 200 | ✅ 201 | ✅ 200 | ✅ 204 |
| Invite (other) | — | ✅ 404 | — | ✅ 404 | ✅ 404 |
| Guest (own) | ✅ 200 | ✅ 200 | ✅ 201 | ✅ 200 | ✅ 204 |
| Guest (other) | — | ✅ 404 | — | ✅ 404 | ✅ 404 |
| Gift (own) | ✅ 200 | ✅ 200 | ✅ 201 | ✅ 200 | ✅ 204 |
| Gift (other) | — | ✅ 404 | — | ✅ 404 | ✅ 404 |
| Message (own) | ✅ 200 | ✅ 200 | — | ✅ 200 | ✅ 204 |
| Message (other) | — | ✅ 404 | — | ✅ 404 | ✅ 404 |
| Wedding (own) | — | ✅ 200 | — | ✅ 200 | — |
| Wedding (other) | — | ✅ 403 | — | ✅ 403 | — |
| Admin-only | ✅ 403 | — | ✅ 403 | — | ✅ 403 |

- Couple is always scoped to their own `weddingId`
- Accessing another wedding's resource → 404 (not found within their scope)
- Admin-only endpoints (list weddings, manage accounts) → 403

### Guest/Anonymous (`GuestAccessTest`)

| Endpoint | Valid slug | Invalid slug | Protected |
|----------|-----------|--------------|-----------|
| `POST /w/{slug}/rsvp/invite/search` | ✅ 200 | ✅ 404 | — |
| `GET /w/{slug}/rsvp/guest` | ✅ 200 | ✅ 404 | — |
| `PATCH /w/{slug}/rsvp/guest/status/{id}` | ✅ 200 | ✅ 404 | — |
| `GET /w/{slug}/gift` | ✅ 200 | ✅ 404 | — |
| `GET /w/{slug}/gift/{id}` | ✅ 200 | ✅ 404 | — |
| `POST /w/{slug}/message` | ✅ 201 | ✅ 404 | — |
| All protected endpoints | — | — | ✅ 401 |

- Public endpoints use slug-based resolution (`/w/{slug}/*`)
- Invalid slug → 404 (wedding not found)
- Any protected endpoint without auth → 401

## Running Tests

```bash
# With formatted output, coverage, and live timer
make test

# Raw Maven
mvn test -DskipTests=false -Dspring.profiles.active=test

# Verbose mode (shows Maven output in real-time)
VERBOSE=1 make test
```

## Coverage

JaCoCo generates coverage reports at `target/site/jacoco/index.html` after `make test`.

Coverage is displayed in the terminal summary:

```
  == Coverage ==
    [COV] Instructions: 78%  |  Branches: 62%
```

## Adding New Tests

1. Use `@WithMockAdmin` or `@WithMockCouple` at class or method level
2. Add seed data to `V4__test_data.sql` if new entities are needed
3. Follow the pattern: `mockMvc.perform(verb("/path")).andExpect(status().isXxx())`
4. Group by domain using `@Nested` + `@DisplayName`
