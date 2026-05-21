# Getting Started

Welcome to **Hestia Core API** — a wedding management platform backend. This guide will get you running in 5 minutes and help you understand the codebase.

---

## 1. Setup

```bash
# Clone and enter the project
git clone <repo-url> && cd core-api

# Install dependencies (skips tests for speed)
make install
```

### Environment

Create a `.env` file in the project root:

```bash
DB_URL=jdbc:postgresql://localhost:5432/hestia_db
DB_USERNAME=your_username
DB_PASSWORD=your_password
SUPABASE_PROJECT_ID=your_supabase_project_id
```

> Don't have a Postgres instance? The test suite uses H2 in-memory — you can explore the API without a real database by running tests.

---

## 2. Run

```bash
make run
```

API starts at `http://localhost:8081/api/v2`

Open Swagger UI to explore all endpoints interactively:
```
http://localhost:8081/api/v2/swagger-ui.html
```

---

## 3. Test the API manually

The project includes ready-to-use API collections in two formats:

### Bruno (recommended)

[Bruno](https://www.usebruno.com/) is a fast, git-friendly API client. The collection lives in `/docs/bruno` and is version-controlled.

```
docs/bruno/
├── account/       # Account CRUD
├── gift/          # Gift registry
├── guest/         # Public guest endpoints
├── message/       # Messages
├── rsvp/          # Invites & guests
├── wedding/       # Wedding management
└── collection.bru # Collection config + auth
```

Open Bruno → **Open Collection** → select the `/docs/bruno` folder. Environments and auth are pre-configured.

### Postman

A Postman collection + environment is available in `/docs/postman`:

```
docs/postman/
├── Hestia Core API.postman_collection.json
└── Hestia Core API.postman_environment.json
```

Import both files into Postman. Set the environment variables (`base_url`, `token`, `wedding_id`).

---

## 4. Verify everything works

```bash
make test    # 114 integration tests, shows coverage
make lint    # Formats code + runs static analysis
```

---

## 5. Understand the architecture

### How requests flow

```
Request → SecurityFilter (JWT) → Controller → Service → Repository → DB
                                      ↓
                              AuthenticatedUser
                              (role + weddingId)
```

### Two types of users

| Role | What they can do |
|------|-----------------|
| **COUPLE** | Manage their own wedding (invites, guests, gifts, messages) |
| **ADMIN** | Access any wedding, manage accounts and weddings |

### Three types of endpoints

| Path                                 | Auth | Who uses it |
|--------------------------------------|------|-------------|
| `/api/v2/*`                          | JWT required | Couple & Admin (dashboard) |
| `/api/v2/w/{slug}/*`                 | Public | Wedding guests (RSVP, view gifts, send messages) |
| `/api/v2/wedding`, `/api/v2/account` | Admin only | Platform management |

### Multi-tenant isolation

Every resource belongs to a `wedding_id`. The system resolves which wedding you're operating on:

- **Couple** → always their own wedding (from JWT)
- **Admin** → passes `?wedding=<id>` for creates, or accesses any resource by id
- **Guest** → resolved from the URL slug (`/w/alice-bob/gift`)

---

## 6. Explore the code

Start here depending on what you want to understand:

| I want to... | Look at |
|--------------|---------|
| See all endpoints | `src/main/java/.../domain/*/controller/` |
| Understand auth rules | `infrastructure/security/config/SecurityConfig.java` |
| See how roles work | `infrastructure/security/principal/AuthenticatedUser.java` |
| Understand tenant scoping | `AuthenticatedUser.resolveWeddingId()` |
| See business logic | `src/main/java/.../domain/*/service/` |
| Check public guest APIs | `src/main/java/.../guest/` |
| Understand the DB schema | `src/main/resources/db/migration/` |
| See how tests work | `src/test/TESTING.md` |

### Domain structure (each domain follows this pattern)

```
domain/rsvp/
├── controller/    ← HTTP layer (thin, delegates to service)
├── service/       ← Business logic
├── repository/    ← Database queries (Spring Data)
├── entity/        ← JPA entities
├── dto/           ← Request/Response objects
├── mapper/        ← Entity ↔ DTO conversion
└── enums/         ← Domain-specific enums
```

---

## 7. Make a change

Typical workflow:

```bash
# 1. Write your code
# 2. Format + lint
make lint

# 3. Run tests
make test

# 4. Commit (using Conventional Commits)
git commit -m "feat: add gift reservation endpoint"
```

### Conventional Commits

We use [Conventional Commits](https://www.conventionalcommits.org/) for clear, parseable history:

```
<type>: <short description>
```

| Type | When to use |
|------|-------------|
| `feat` | New feature |
| `fix` | Bug fix |
| `refactor` | Code change that doesn't fix a bug or add a feature |
| `test` | Adding or updating tests |
| `docs` | Documentation only |
| `chore` | Build, config, dependencies |

Examples:
```
feat: add GET /rsvp/guest/{id} endpoint
fix: resolve enum cast error on H2 with custom domains
refactor: move role check to AuthenticatedUser.hasRole()
test: add tenant isolation tests for couple role
docs: update getting started guide
chore: add Spotless and Checkstyle plugins
```

The linter auto-formats your code (Palantir Java Format), checks naming/imports (Checkstyle), and scans for bugs (SpotBugs) — all in one command.

---

## 8. Key files to know

| File | Why it matters |
|------|---------------|
| `Makefile` | All dev commands (`test`, `lint`, `install`, `run`) |
| `pom.xml` | Dependencies and build plugins |
| `SecurityConfig.java` | Which endpoints require which roles |
| `AuthenticatedUser.java` | Role checking + wedding resolution logic |
| `GlobalExceptionHandler.java` | How errors become HTTP responses |
| `BaseModel.java` | Shared fields: `id`, `createdAt`, `updatedAt`, `isActive` |
| `V4__test_data.sql` | Seed data used in tests |

---

## Next steps

- Read the [ROADMAP.md](ROADMAP.md) to see what's built and what's planned
- Check [src/test/TESTING.md](../src/test/TESTING.md) for the full test matrix
- Open Swagger UI and try the endpoints with a real JWT
