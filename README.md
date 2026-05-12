# Hestia Core API

A Spring Boot REST API for managing wedding/event planning features including RSVP management, gift registry, and guest messaging.

## 📋 Project Overview

**Hestia Core API** is a comprehensive event management platform built with Spring Boot 3.5.14 and Java 21. The application provides RESTful endpoints for managing invites, guests, gift registries, and guest messages.

### Current Features

- **RSVP Management**: Invite and guest tracking with status management
- **Gift Registry**: Gift catalog with availability tracking
- **Messaging System**: Guest message management
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
- **Additional Libraries**: Lombok, Spring DevTools

## 🚀 Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.6+
- PostgreSQL database

### Environment Variables

Create a `.env` file or set the following environment variables:

```bash
DB_URL=jdbc:postgresql://localhost:5432/hestia_db
DB_USERNAME=your_username
DB_PASSWORD=your_password
SUPABASE_PROJECT_ID=your_supabase_project_id
```

### Running the Application

```bash
# Build the project
./mvnw clean install

# Run the application
./mvnw spring-boot:run
```

The API will be available at: `http://localhost:8081/api/v2`

### API Documentation

Once the application is running, access the Swagger UI at:
```
http://localhost:8081/api/v2/swagger-ui.html
```

## 📂 Project Structure

```
src/main/java/com/hestia/api/
├── common/                  # Shared components
│   ├── dto/                # Common DTOs (PageResponse)
│   ├── exception/          # Custom exceptions
│   ├── mapper/             # Utility mappers
│   └── model/              # Base models
├── domain/
│   ├── accounts/           # User management (entity, roles, CRUD)
│   ├── rsvp/              # RSVP management (Invites & Guests)
│   ├── registry/          # Gift registry management
│   └── message/           # Guest messaging system
├── infraestructure/
│   └── security/          # Security layer
│       ├── config/        # SecurityConfig (endpoint rules)
│       ├── filter/        # JwtAuthenticationFilter
│       ├── jwt/           # Supabase JWT validation & claims
│       ├── principal/     # AuthenticatedUser (UserDetails)
│       └── service/       # AuthenticatedUserService
└── CoreApiApplication.java
```

## 🗺️ Development Roadmap

This project follows an **8-step incremental development approach**. Each step represents a major feature milestone and will be developed on a separate branch before merging to production.

### Branch Strategy

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
- **Invites**: `/api/v2/invites`
- **Guests**: `/api/v2/guests`
- **Gifts**: `/api/v2/gifts`
- **Messages**: `/api/v2/messages`

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

## 📍 Step 3: Multi-tenant

**Branch**: `step-3-multi-tenant`  
**Status**: Planned

### Deliverables
- [ ] Tenant identification strategy (subdomain/header/path)
- [ ] Tenant context management
- [ ] Database schema per tenant or shared schema with wedding_id
- [ ] Tenant-aware repositories and queries
- [ ] Tenant registration and management
- [ ] Data isolation between tenants
- [ ] Tenant-specific configuration

### Technical Approach
- Hibernate multi-tenancy support
- Tenant resolver implementation
- Connection provider per tenant
- Tenant interceptor/filter

---

## 📍 Step 4: CRUD Role & Tenant Based

**Branch**: `step-4-role-tenant-crud`  
**Status**: Planned

### Deliverables
- [ ] Role entity and management (Admin, Organizer, Guest, etc.)
- [ ] Permission-based access control
- [ ] Method-level security annotations
- [ ] Tenant + Role combination authorization
- [ ] User-Role-Tenant relationship mapping
- [ ] Endpoint protection based on roles
- [ ] Audit logging for sensitive operations

### Technical Approach
- `@PreAuthorize` and `@Secured` annotations
- Custom security expressions
- Role hierarchy configuration
- Tenant-scoped role assignments

---

## 📍 Step 5: Payment Gateway

**Branch**: `step-5-payment-gateway`  
**Status**: Planned

### Deliverables
- [ ] Payment gateway integration (Asaas)
- [ ] Payment processing endpoints
- [ ] Order/Transaction entity
- [ ] Payment status tracking
- [ ] Webhook handling for payment events
- [ ] Refund management
- [ ] Payment history and receipts
- [ ] Gift contribution payments

### Technical Approach
- Asaas SDK or similar payment provider
- Idempotency for payment operations
- Secure API key management
- Payment event listeners

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

---

## 🔧 Development Guidelines

### Code Standards

- Follow Java naming conventions
- Use Lombok to reduce boilerplate
- Write meaningful commit messages
- Keep controllers thin, business logic in services
- Use DTOs for API contracts
- Handle exceptions gracefully

### API Conventions

- Base URL: `/api/v2`
- Response Format: JSON with snake_case
- Pagination: Query parameters `page` and `size`
- Error Format: Standardized error responses
- HTTP Status Codes: RESTful conventions
