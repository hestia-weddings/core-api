# Hestia Core API

A Spring Boot REST API for managing wedding/event planning features including RSVP management, gift registry, and guest messaging.

## 📖 Documentation

| Document | Description |
|----------|-------------|
| [docs/GETTING_STARTED.md](docs/GETTING_STARTED.md) | New here? Start here — setup, architecture, and where to look |
| [docs/ROADMAP.md](docs/ROADMAP.md) | Development roadmap with all steps |
| [docs/PAYMENT.md](docs/PAYMENT.md) | Payment gateway integration (Asaas Checkout) |
| [src/test/TESTING.md](src/test/TESTING.md) | Test suite documentation |


## 📋 Project Overview

**Hestia Core API** is a comprehensive event management platform built with Spring Boot 3.5.14 and Java 21. The application provides RESTful endpoints for managing invites, guests, gift registries, and guest messages.

### Current Features

- **RSVP Management**: Invite and guest tracking with status management
- **Gift Registry**: Gift catalog with availability tracking
- **Messaging System**: Guest message management
- **Multi-tenant**: Wedding-scoped data isolation with admin cross-tenant access
- **Public Guest APIs**: Slug-based guest endpoints (`/w/{slug}/*`)
- **API Documentation**: Swagger/OpenAPI integration

### Tech Stack

- **Framework**: Spring Boot 3.5.14
- **Language**: Java 21
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA / Hibernate
- **Security**: Spring Security 6.x (RBAC, stateless)
- **Authentication**: Supabase (JWT via JWK Set URI, ES256)
- **Documentation**: SpringDoc OpenAPI (Swagger UI)
- **Build Tool**: Maven
- **Code Quality**: Spotless (Palantir), Checkstyle, SpotBugs, JaCoCo
- **Testing**: JUnit 5, MockMvc, H2 (PostgreSQL mode), Flyway
- **Additional Libraries**: Lombok, Spring DevTools

## 📂 Project Structure

```
src/main/java/com/hestia/api/
├── common/                  # Shared components
│   ├── dto/                # Common DTOs (PageResponse, ErrorResponse)
│   ├── exception/          # Global exception handler & custom exceptions
│   ├── mapper/             # Utility mappers (PageMapper)
│   └── model/              # Base entity model (BaseModel)
├── domain/
│   ├── accounts/           # User management
│   ├── rsvp/              # RSVP management (Invites & Guests)
│   ├── registry/          # Gift registry
│   ├── message/           # Guest messaging
│   └── wedding/           # Wedding management
├── guest/                   # Public guest-facing endpoints (/w/{slug}/*)
├── infrastructure/
│   ├── config/             # OpenAPI, WebMvc config
│   └── security/
│       ├── config/         # SecurityConfig (endpoint rules)
│       ├── filter/         # JwtAuthenticationFilter
│       ├── jwt/            # Supabase JWT validation & claims
│       ├── principal/      # AuthenticatedUser (UserDetails)
│       └── service/        # AuthenticatedUserService
└── CoreApiApplication.java
```

## 🗺️ Development Roadmap

> 📖 See full roadmap: [ROADMAP.md](docs/ROADMAP.md)

| Step | Feature | Status |
|------|---------|--------|
| 1 | CRUD + Swagger Documentation | ✅ Completed |
| 2 | Authorization (RBAC) | ✅ Completed |
| 3 | Multi-tenant | ✅ Completed |
| 4 | Test Suite | ✅ Completed |
| 5 | Payment Gateway | 🔲 Planned |
| 6 | Infra & Deployment | 🔲 Planned |
| 7 | Monitoring & Observability | 🔲 Planned |
| 8 | SMTP & Email (Optional) | 🔲 Optional |

### Available Commands

| Command | Description |
|---------|-------------|
| `make install` | Clean install (skip tests) |
| `make run` | Start the application |
| `make test` | Run tests with coverage report |
| `make test-no-lint` | Run tests only (skip formatting/linting, faster) |
| `make lint` | Format code + run Checkstyle and SpotBugs |
