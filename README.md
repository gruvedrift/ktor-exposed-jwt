# Ktor + Exposed + JWT

This project is a clean, minimal, and extensible example of how to build a secure Kotlin backend using Ktor with:

- Multiple datasources using Exposed.
- Multiple environments through Ktor `application.config`
- Custom JWT-based authentication & authorization with role-based access control.
- Custom status pages & error handling for a better developer and client experience.
- How to write meaningful tests leveraging Ktor's `testApplication{}` together with dedicated test databases with
  synthetic data.
- A short implementation comparison of Ktor and its larger bloated counterpart, Spring Boot.

### Requirements:

- Docker technology ( [Docker Desktop](https://www.docker.com/products/docker-desktop) ,
  [Podman](https://www.docker.com/products/docker-desktop) or [Colima](https://github.com/abiosoft/colima) )
- JDK 21
- Gradle

### Running the Application

To start the application with its dependencies (PostgreSQL database), use the provided script:

```bash
./up.sh
```

This script will:

1. Attempt to use Docker Compose to spin up the PostgreSQL container.
2. If Docker is not available, it will fall back to Podman Compose (for systems using Podman).
3. Start the Ktor application once the database is ready.

To stop and clean up containers, use:

```bash
./down.sh
```

### Running the tests

To run unit and integration tests in an isolated environment:

```bash
./test.sh
```

This script will:

1. Start a disposable PostgreSQL test container.
2. Apply database migrations.
3. Run all test suites (unit + integration).
4. Clean up the container and test resources after execution.

### Multiple Datasources with Exposed

This project demonstrates how simple it is to configure and use multiple datasources with Exposed.
Instead of relying on exposed global connection management, we opt to pass Database instances explicitly into the
repository or the transaction blocks.

Each repository is isolated to its own Database connection, making it easy to separate the two domains ( Podracing /
Authentication ). Transactions are scoped to the correct datasource, keeping the persistence layer clean and explicit.
The databases are initialized through a common [DatabaseFactory](src/main/kotlin/config/DatabaseFactory.kt)
implementation.

### Simple Multi-Environment Configuration with Ktor

Ktor makes managing multiple environments  (development, testing) straightforward without the overhead of a large
framework like Spring Boot.
Configuration is handled in a single HOCON [application.conf](src/main/resources/application.conf) file with separate
blocks for each environment.
Each environment can define its own databases, JWT configuration, and deployment settings.

### Custom JWT Authentication & Authorization

The project features a custom [authentication](src/main/kotlin/plugins/Authentication.kt) flow with signed JWT tokens
and [role-based access control](src/main/kotlin/service/AuthService.kt) (RBAC):

```
Users authenticate by sending their username/password to a token endpoint.

Passwords are securely verified using bcrypt hashing.

On success, the server returns a signed JWT containing the username and role as claims.

Protected routes validate the JWT and enforce RBAC by checking the role claim.

```

In this repository the secret key is stored in the `application.conf` for simplicity.
In a production environment, one should use environment variables or a secret manager( e.g., GitHub Secrets or Azure Key
Vault )for secure key management.
Passwords for the test users are commented in the migration script.

### Custom status pages

Centralized error handling with Ktor’s StatusPages feature, makes debugging and API usage more predictable, consistent
and testable. Error handling is streamlined with custom Ktor [status pages](src/main/kotlin/plugins/StatusPages.kt).
Instead of returning generic error messages, the application provides meaningful HTTP responses and messages for invalid
requests, authentication failures, or unexpected errors.

This approach makes your API more user-friendly and debuggable while keeping the controller / presentation layer clean
and uncluttered.

### Full-Flow / E2E Integration Testing with Ktor

Ktor makes full HTTP-level integration testing a breeze thanks to its built-in `testApplication{}` block, which spins up
a lightweight embedded server for every test.
This allows us to write meaningful tests that can test full request/response cycles.
You can test your routes, authentication, and JSON serialization end-to-end no-mocks required (JTRH-NBR).

This project uses a `BaseTestConfiguration()` to handle:

- Initializing multiple database contexts (podracing + authentication) for integration tests.
- Running migrations once per test class for performance.
- Ensuring clean teardown to avoid leaking database connections.

The project also demonstrates two Test styles:

1) **Verbose, Explicit:**  
   [PodracerIntegrationTest](src/test/kotlin/PodracerIntegrationTest.kt)
   Each test spins up its own `testApplication {}` block and configures the client and module inline.
2) **(Preferred) Concise, Reusable Pattern:**  
   [EngineIntegrationTest](src/test/kotlin/EngineIntegrationTest.kt)
   A cleaner pattern extracts a helper function `runEngineTest` to reduce repetitive setup. This way the tests become
   one-liners, focused purely on behavior.

### Local Multi-Database Setup with Docker Compose

To make development and testing frictionless, this project uses [Docker Compose](docker-compose.yml) to spin up isolated
PostgreSQL databases
for both the application and authentication services.

### API Testing with Postman

A Postman collection is included under `postman/Podracer Auth & Registration API.postman_collection.json`.

1. Import the JSON file into Postman.
2. Run `Auth - Login` to obtain a JWT. You can use any credentials from the test users provided in the .sql migration
   script.
3. Use the JWT for protected endpoints such as `Register Bozzie Baranta` `/api/podracer/analytics/{id}`.

### Technologies / patterns used

| Tool / Concept                                                            | Purpose                                                                              |
|---------------------------------------------------------------------------|--------------------------------------------------------------------------------------|
| [Ktor](https://ktor.io)                                                   | Kotlin-based web framework                                                           |
| [Flyway](https://flywaydb.org/)                                           | Database version control and migrations                                              |
| [Exposed](https://github.com/JetBrains/Exposed)                           | Type-safe SQL DSL and DAO support in Kotlin                                          |
| [Status pages](https://ktor.io/docs/server-status-pages.html)             | Intercepts thrown exceptions and returns custom responses                            |
| [Routing](https://start.ktor.io/p/routing)                                | Provides a structured routing DSL                                                    |
| [Content Negotiation](https://start.ktor.io/p/content-negotiation)        | Provides automatic content conversion according to Content-Type and Accept headers   |
| [kotlinx.serialization](https://start.ktor.io/p/kotlinx-serialization)    | Handles JSON serialization using kotlinx.serialization library                       |
| [PostgreSQL](https://www.postgresql.org/)                                 | Relational database                                                                  |
| [Docker Compose](https://docs.docker.com/compose/)                        | Container orchestration for dev and test environment                                 |
| [JUnit 5](https://junit.org/junit5/)                                      | Testing framework for unit and integration tests                                     |
| [Ktor TestApplication](https://ktor.io/docs/server-testing.html#test-app) | Lightweight embedded test server for E2E and integration testing                     |
| [JWT](https://ktor.io/docs/server-jwt.html#flow)                          | Securely transmitting information between parties. Also used for Authorization       |
| Custom Exceptions                                                         | Used for clearer domain-level error semantics                                        |
| HikariCP                                                                  | Lightweight, high-performance connection pool                                        |
| Typesafe Config                                                           | Easy access to environment-specific settings                                         |
| Multi-Database Contexts                                                   | Explicit database connections per repository for Podracing and Authentication domain |
| Layered Architecture (Controller &rarr; Service &rarr; Repository)        | Clear separation of concerns, keeps domain logic isolated from I/O layers            |
| Role-Based Access Control (RBAC)                                          | Enforces route-level access policies based on JWT `role` claim                       |
| Dockerized Local Development                                              | Standardizes dev/test environment across machines                                    |

### Ktor vs Spring Boot – Key Differences in This Project

In this project, Ktor is used without a DI framework, emphasizing explicit wiring and compositional style.

| Feature                     | Ktor                                         | Spring Boot                           |
|-----------------------------|----------------------------------------------|---------------------------------------|
| **Routing**                 | Declarative DSL (`route {}` blocks)          | Annotations (`@RestController`)       |
| **Dependency Injection**    | Manual (or via Koin/Dagger if added)         | Built-in (Spring Context)             |
| **Serialization**           | `ContentNegotiation + kotlinx.serialization` | `@RequestBody` / Jackson/Gson         |
| **Error Handling**          | `StatusPages`                                | `@ControllerAdvice`, `ResponseEntity` |
| **DB Integration**          | Exposed DSL                                  | Spring Data JPA / JDBC                |
| **Startup & Configuration** | `fun Application.module()` explicitly        | `@SpringBootApplication` auto-setup   |

**Note:** The authentication and role-based authorization flow is a good candidate for a Domain-Driven Design (DDD).
However, for this project, I opted to retain a layered architecture (controller &rarr; service &rarr; repository) to
maintain consistency with the rest of the codebase.