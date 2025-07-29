# ktor-jdbc-jwt-testcontainers

This repository shows how easy it is with Exposed to work on multiple data sources. 
How easy it is to: 
implementing a custom login system with JWT-based access control, which is completely valid and idiomatic for many Ktor-based APIs or internal systems.
How easy it is to setup a dev / test or whatever else environment you'd like with HOCON configuration


### JWT 
For this project the secret is stored in a config file, but should ofcourse be stored as an environment variable on the running server. 

### Multiple datasources 
Instead of relying on exposed global connection management, it is better to pass Database instances explicitly 
into the repository or the transaction blocks.


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

### Technologies / patterns used

| Tool                                                                   | Purpose                                                                            |
|------------------------------------------------------------------------|------------------------------------------------------------------------------------|
| [Ktor](https://ktor.io)                                                | Kotlin-based web framework                                                         |
| [Flyway](https://flywaydb.org/)                                        | Database version control and migrations                                            |
| [Exposed](https://github.com/JetBrains/Exposed)                        | Type-safe SQL DSL and DAO support in Kotlin                                        |
| [Status pages](https://ktor.io/docs/server-status-pages.html)          | Intercepts thrown exceptions and returns custom responses                          |
| [Routing](https://start.ktor.io/p/routing)                             | Provides a structured routing DSL                                                  |
| [Content Negotiation](https://start.ktor.io/p/content-negotiation)     | Provides automatic content conversion according to Content-Type and Accept headers |
| [kotlinx.serialization](https://start.ktor.io/p/kotlinx-serialization) | Handles JSON serialization using kotlinx.serialization library                     |
| [PostgreSQL](https://www.postgresql.org/)                              | Relational database                                                                |
| [Docker Compose](https://docs.docker.com/compose/)                     | Container orchestration for dev and test environment                               |
| Custom Exceptions                                                      | Used for clearer domain-level error semantics                                      |
| HikariCP                                                               | Lightweight, high-performance connection pool                                      |
| Typesafe Config                                                        | Easy access to environment-specific settings                                       |
| [JWT](https://ktor.io/docs/server-jwt.html#flow)                       | Securely transmitting information between parties. Also used for Authorization     |

### IDEAS

- Using Exposed: Exposed is a lightweight SQL library on top of a JDBC driver for the Kotlin programming language.
  Cose to use the DSL
- Add another datasource to check that one can, in fact have two datasources in Exposed.  (JWT)

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

