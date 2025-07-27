# ktor-jdbc-jwt-testcontainers

### Requirements:

- Docker technology ( [Docker Desktop](https://www.docker.com/products/docker-desktop) ,
  [Podman](https://www.docker.com/products/docker-desktop) or [Colima](https://github.com/abiosoft/colima) )
- JDK 21
- Gradle

### Technologies / patterns used

| Tool               | Purpose                                                   |
|--------------------|-----------------------------------------------------------|
| HikariCP           | Lightweight, high-performance connection pool             |
| Flyway             | Database version control and migrations                   |
| Exposed            | Type-safe SQL DSL and DAO support in Kotlin               |
| Typesafe Config    | Easy access to environment-specific settings              |
| Ktor `StatusPages` | Intercepts thrown exceptions and returns custom responses |
| Custom Exceptions  | Used for clearer domain-level error semantics             |

## Building & Running application / tests:

- **Application**: Use the `up.sh` script to start the PostgreSQL database and launch the application.
  The script first tries Docker Compose. If unavailable, it falls back to Podman Compose. Use the `down.sh` to tear down
  the running containers.
- **Tests**: Use the `test.sh` script. It starts a fresh test container with test data to run both unit and integration
  tests against. The script will clean up resources after the tests are run.

## Features

- Ephemeral/Disposable Test DB. Used when running tests in application.

Here's a list of features included in this project:

| Name                                                                   | Description                                                                        |
|------------------------------------------------------------------------|------------------------------------------------------------------------------------|
| [Routing](https://start.ktor.io/p/routing)                             | Provides a structured routing DSL                                                  |
| [Content Negotiation](https://start.ktor.io/p/content-negotiation)     | Provides automatic content conversion according to Content-Type and Accept headers |
| [kotlinx.serialization](https://start.ktor.io/p/kotlinx-serialization) | Handles JSON serialization using kotlinx.serialization library                     |
| [Postgres](https://start.ktor.io/p/postgres)                           | Adds Postgres database to your application                                         |

### IDEAS

- Using Exposed: Exposed is a lightweight SQL library on top of a JDBC driver for the Kotlin programming language.
  Cose to use the DSL
- Add another datasource to check that one can, in fact have two datasources in Exposed.  (JWT)

| Component                                                                | Description                                 |
|--------------------------------------------------------------------------|---------------------------------------------|
| [Ktor](https://ktor.io)                                                  | Kotlin-based web framework                  |
| [Exposed](https://github.com/JetBrains/Exposed)                          | SQL DSL for database access                 |
| [PostgreSQL](https://www.postgresql.org/)                                | Relational database                         |
| [Flyway](https://flywaydb.org/)                                          | Schema migration tool                       |
| [Testcontainers](https://www.testcontainers.org/)                        | Launches disposable containers for testing  |
| [Docker Compose](https://docs.docker.com/compose/)                       | Container orchestration for dev environment |
| [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) | JSON serialization                          |

### Differences from spring boot:

- Ktor ( without dependency injection ) does not rely on annotations like `@Controller` `@Bean` etc. It follows a
  declarative and compositional style,
  where we explicitly register features like routing by extending the Application
  function (`Application.installRoutes()`).
- Ktor handles Serialization through the `ContentNegotiation` plugin. Instead of wrapping the response in
  a `ResponseEntity` class, the http responses
  are created directly through the
- StatusPages plugin (Ktor’s global error handler)