package com.gruvedrift.config

import com.gruvedrift.Environment
import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import java.sql.Connection

/**
 * DatabaseConfig is declared as an `object`, making it a thread-safe singleton in Kotlin.
 * This ensures the database is configured only once per application lifecycle.
 *
 * Unlike Spring Boot, which typically injects a DataSource into each repository,
 * Ktor with Exposed uses a globally configured connection via `Database.connect()`.
 *
 * After initialization, the `Exposed` ORM automatically uses this connection in every `transaction {}` block.
 *
 * This design avoids common pitfalls found in Spring-based applications, such as failed @Transactional rollbacks
 * or configuration complexity when working with multiple data sources.
 *
 * Flyway is used to run migrations automatically on startup.
 */
object DatabaseConfig {

    private var datasource: HikariDataSource? = null

    /**
     * Initializes the database connection pool using HikariCP and configuration
     * based on the active environment (e.g., dev or test).
     */
    fun init(environment: Environment = Environment.DEV) {
        if (datasource == null) {

            val configurationParams = ConfigFactory.load().getConfig(environment.environmentName)

            val hikariConfig = HikariConfig().apply {
                driverClassName = configurationParams.getString("driver")
                jdbcUrl = configurationParams.getString("url")
                username = configurationParams.getString("username")
                password = configurationParams.getString("password")
                maximumPoolSize = 10

            }
            datasource = HikariDataSource(hikariConfig)
        }

        // Run Flyway DB migrations
        Flyway.configure()
            .dataSource(datasource)
            .locations("classpath:db/migration")
            .load()
            .migrate()

        // Connect to Exposed global DB connection
        datasource?.let { Database.connect(it) }
            ?: throw IllegalStateException("Datasource initialization failed!")
    }

    /**
     * Properly close the datasource and clean up resources (e.g., between tests).
     */
    fun close() {
        datasource?.close()
        datasource = null
    }
}