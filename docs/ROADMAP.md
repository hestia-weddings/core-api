# 🗺️ Development Roadmap

This project follows an **8-step incremental development approach**. Each step represents a major feature milestone and will be developed on a separate branch.

## Branch Strategy

- `prod` - Production-ready code
- `step-1-crud-swagger` - Step 1 deliverable
- `step-2-authorization` - Step 2 deliverable
- `step-3-multi-tenant` - Step 3 deliverable
- And so on...

---

## 📍 Step 1: CRUD + Swagger Documentation ✅

**Branch**: `step-1-crud-swagger`  
**Status**: Completed

### Deliverables
- ✅ Complete CRUD operations for all domains (RSVP, Registry, Messages)
- ✅ RESTful API endpoints with proper HTTP methods
- ✅ Swagger/OpenAPI documentation integrated
- ✅ Request/Response DTOs
- ✅ Exception handling
- ✅ Pagination support

### Endpoints
- **Invites**: `/api/v2/rsvp/invite`
- **Guests**: `/api/v2/rsvp/guest`
- **Gifts**: `/api/v2/gift`
- **Messages**: `/api/v2/message`
- **Weddings (admin-only)**: `/api/v2/wedding`
- **Accounts (admin-only)**: `/api/v2/account`

---

## 📍 Step 2: Authorization (RBAC) ✅

**Branch**: `step-2-authorization`  
**Status**: Completed

### Deliverables
- ✅ Spring Security integration (stateless, no sessions)
- ✅ Supabase JWT validation via JWK Set URI
- ✅ Role-based access control (COUPLE, ADMIN)
- ✅ Endpoint-level authorization rules
- ✅ User entity with `authUserId` linked to Supabase
- ✅ Custom `JwtAuthenticationFilter`
- ✅ Public endpoints for guest-facing features (RSVP search, gift listing, messages)

### Technical Approach
- **Authentication**: Delegated to Supabase (JWT issuance, OAuth, user management)
- **Authorization**: Spring Security 6.x with RBAC
- Supabase JWT decoded and validated using JWK Set URI (ES256)
- User loaded from DB by `authUserId` to resolve roles
- `SecurityConfig` defines per-endpoint access rules based on roles
- No login/register endpoints in this project — handled entirely by Supabase

---

## 📍 Step 3: Multi-tenant ✅

**Branch**: `step-3-multi-tenant`  
**Status**: Completed

### Deliverables
- ✅ Tenant identification via `wedding_id` (shared schema approach)
- ✅ Slug-based tenant resolution for public/guest endpoints (`/w/{slug}/*`)
- ✅ `WeddingIdResolver` — Couple uses own `weddingId` from JWT, Admin must pass `wedding_id` param
- ✅ `SlugResolver` interceptor — resolves slug → `weddingId` for guest-facing routes
- ✅ Tenant-aware repositories (all queries scoped by `wedding_id`)
- ✅ Data isolation between weddings (Couple cannot access other wedding's data)
- ✅ Admin cross-tenant access with explicit `wedding_id` parameter

### Technical Approach
- **Strategy**: Shared schema with `wedding_id` FK on all tenant-scoped tables
- **Couple**: `weddingId` extracted from `AuthenticatedUser` (resolved from JWT → DB user)
- **Admin**: Must provide `wedding_id` as query param (returns 400 if missing)
- **Guest (public)**: `SlugResolver` HandlerInterceptor resolves `/w/{slug}` → `weddingId` via request attribute
- **Validation**: Controllers use `AuthenticatedUser.resolveWeddingId(weddingId)` to enforce tenant scope

---

## 📍 Step 4: Test Suite (RBAC & Tenant Validation) ✅

> 📖 Full test documentation: [src/test/TESTING.md](../src/test/TESTING.md)

**Branch**: `step-4-test-suite`  
**Status**: Completed

### Deliverables
- ✅ Integration test infrastructure (H2 in-memory, Flyway test migrations)
- ✅ Custom security annotations (`@WithMockAdmin`, `@WithMockCouple`)
- ✅ Role-based access tests (Admin, Couple, Guest/Anonymous)
- ✅ Tenant isolation tests (couple scoped to own wedding, admin cross-tenant)
- ✅ Public slug endpoint tests (valid/invalid slug resolution)
- ✅ JaCoCo coverage reporting
- ✅ `make test` with colored output, live timer, and coverage summary

### Technical Approach
- `@SpringBootTest` + `@AutoConfigureMockMvc` + `@Transactional` (rollback per test)
- H2 with `MODE=PostgreSQL` and custom domains for enum types
- `MockSecurityContextFactory` builds real `AuthenticatedUser` principal from annotations
- Flyway migrations (test-only) with seed data for 2 weddings, 2 users, sample entities
- JaCoCo for coverage (`target/site/jacoco/index.html`)

---

## 📍 Step 5: Payment Gateway ✅

> 📖 Full payment documentation: [docs/PAYMENT.md](./PAYMENT.md)

**Branch**: `step-5-payment-gateway`  
**Status**: Completed

### Deliverables
- ✅ Payment gateway integration (Asaas Checkout + Transfer API)
- ✅ Centralized Héstia Asaas account (single API key from env config)
- ✅ Wallet entity per wedding (PIX key, balance in cents, fee in basis points)
- ✅ Order entity with status tracking (PENDING → PAID/EXPIRED/FAILED)
- ✅ Transaction entity for withdrawals (PENDING → COMPLETED/FAILED)
- ✅ Guest checkout endpoint (`POST /w/{slug}/gift/{giftId}/checkout`)
- ✅ Webhook handling for payment + transfer events
- ✅ Wallet balance crediting on `CHECKOUT_PAID`
- ✅ Transfer endpoint for couple PIX withdrawal
- ✅ Fee deduction at withdrawal time (configurable %, default 5%)
- ✅ Available balance prevents over-withdrawal
- ✅ Idempotent webhook processing
- ✅ Integration tests (158 total, 88% instruction coverage)

### Technical Approach
- **Provider**: Asaas Checkout + Transfer API (PCI-DSS hosted page, PIX + Credit Card)
- **Credential model**: Single centralized Héstia Asaas API key (from `application.properties` / env var)
- **Wallet**: One per wedding — stores PIX key, accumulated balance (cents), fee (basis points)
- **Checkout flow**: Backend creates Asaas Checkout → guest pays → webhook credits wallet balance
- **Transfer flow**: Couple requests withdrawal → backend calls Asaas Transfer API → webhook confirms
- **Fee model**: Fee invisible to couple; deducted at withdrawal time from gross balance
- **Available balance**: `wallet.balance - SUM(pending transactions)` — prevents over-withdrawal
- **Stock management**: Automatic via `gift_availability` DB view (counts PAID orders)
- **Webhook validation**: Global webhook token from config (single token for all weddings)
- **Idempotency**: Status transitions (PENDING→PAID, PENDING→COMPLETED) happen exactly once
- **HTTP client**: Spring `RestClient` with error wrapping (`AsaasCheckoutException` / `AsaasTransferException` → 502)
- **Testing**: `@MockitoBean` on Asaas clients for integration tests, `MockRestServiceServer` for client unit tests

---

## 📍 Step 6: Infra & Deployment

**Branch**: `step-6-infra-deployment`  
**Status**: Planned

### Deliverables
- [ ] Dockerization (Dockerfile + docker-compose)
- [ ] CI/CD pipeline (GitHub Actions/GitLab CI)
- [ ] Cloud deployment configuration (AWS/GCP/Azure)
- [ ] Environment-specific configurations
- [ ] Database migration strategy (Flyway/Liquibase)
- [ ] Load balancing and scaling setup
- [ ] SSL/TLS configuration
- [ ] Backup and disaster recovery plan

### Technical Approach
- Multi-stage Docker builds
- Kubernetes manifests or cloud-native deployment
- Infrastructure as Code (Terraform/CloudFormation)
- Automated testing in CI pipeline

---

## 📍 Step 7: Monitoramento & Observabilidade

**Branch**: `step-7-monitoring-observability`  
**Status**: Planned

### Deliverables
- [ ] Application metrics (Micrometer + Prometheus)
- [ ] Health check endpoints (Spring Actuator)
- [ ] Distributed tracing (Zipkin/Jaeger)
- [ ] Centralized logging (ELK Stack or similar)
- [ ] Performance monitoring (APM tools)
- [ ] Custom business metrics
- [ ] Alerting configuration
- [ ] Dashboards (Grafana)

### Technical Approach
- Spring Boot Actuator
- Micrometer registry
- Structured logging (JSON format)
- Correlation IDs for request tracing

---

## 📍 Step 8: SMTP & Email Integration (Optional)

**Branch**: `step-8-email-integration`  
**Status**: Optional

### Deliverables
- [ ] SMTP configuration (SendGrid/AWS SES/Mailgun)
- [ ] Email template engine (Thymeleaf/FreeMarker)
- [ ] Transactional emails (guest confirmations, reminders)
- [ ] Email queue management
- [ ] Retry mechanism for failed emails
- [ ] Email tracking and analytics
- [ ] Unsubscribe management
- [ ] Bulk email support

### Technical Approach
- Spring Mail integration
- Async email sending
- Template-based email generation
- Email service abstraction layer
