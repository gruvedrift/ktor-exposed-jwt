# ktor-jdbc-jwt-testcontainers

This project was created using the [Ktor Project Generator](https://start.ktor.io).

Here are some useful links to get you started:

- [Ktor Documentation](https://ktor.io/docs/home.html)
- [Ktor GitHub page](https://github.com/ktorio/ktor)
- The [Ktor Slack chat](https://app.slack.com/client/T09229ZC6/C0A974TJ9). You'll need
  to [request an invite](https://surveys.jetbrains.com/s3/kotlin-slack-sign-up) to join.

## Features

Here's a list of features included in this project:

| Name                                                                   | Description                                                                        |
|------------------------------------------------------------------------|------------------------------------------------------------------------------------|
| [Routing](https://start.ktor.io/p/routing)                             | Provides a structured routing DSL                                                  |
| [Authentication](https://start.ktor.io/p/auth)                         | Provides extension point for handling the Authorization header                     |
| [Authentication JWT](https://start.ktor.io/p/auth-jwt)                 | Handles JSON Web Token (JWT) bearer authentication scheme                          |
| [Content Negotiation](https://start.ktor.io/p/content-negotiation)     | Provides automatic content conversion according to Content-Type and Accept headers |
| [kotlinx.serialization](https://start.ktor.io/p/kotlinx-serialization) | Handles JSON serialization using kotlinx.serialization library                     |
| [Postgres](https://start.ktor.io/p/postgres)                           | Adds Postgres database to your application                                         |

## Building & Running

To build or run the project, use one of the following tasks:

| Task                          | Description                                                          |
|-------------------------------|----------------------------------------------------------------------|
| `./gradlew test`              | Run the tests                                                        |
| `./gradlew build`             | Build everything                                                     |
| `buildFatJar`                 | Build an executable JAR of the server with all dependencies included |
| `buildImage`                  | Build the docker image to use with the fat JAR                       |
| `publishImageToLocalRegistry` | Publish the docker image locally                                     |
| `run`                         | Run the server                                                       |
| `runDocker`                   | Run using the local docker image                                     |

If the server starts successfully, you'll see the following output:

```
2024-12-04 14:32:45.584 [main] INFO  Application - Application started in 0.303 seconds.
2024-12-04 14:32:45.682 [main] INFO  Application - Responding at http://0.0.0.0:8080
```

### IDEAS

- Add tests
- Using Exposed: Exposed is a lightweight SQL library on top of a JDBC driver for the Kotlin programming language.
Cose to use the DSL 
- try out a try / catch style controller
- Add another datasource to check that one can, in fact have two datasources in Exposed.  (JWT)
- Ephemeral/Disposable Test DB. Used when running tests in application. ( full integration test )
- Add logging, especially the exceptions and write to log output file. 

```kotlin
post {
    try {
        val task = call.receive<Task>()
        repository.addTask(task)
        call.respond(HttpStatusCode.NoContent)
    } catch (ex: IllegalStateException) {
        call.respond(HttpStatusCode.BadRequest)
    } catch (ex: JsonConvertException) {
        call.respond(HttpStatusCode.BadRequest)
    }
}
```

### Differences from spring boot:

- Ktor ( without dependency injection ) does not rely on annotations like `@Controller` `@Bean` etc. It follows a
  declarative and compositional style,
  where we explicitly register features like routing by extending the Application
  function (`Application.installRoutes()`).
- Ktor handles Serialization through the `ContentNegotiation` plugin. Instead of wrapping the response in
  a `ResponseEntity` class, the http responses
  are created directly through the 
- StatusPages plugin (Ktor’s global error handler)