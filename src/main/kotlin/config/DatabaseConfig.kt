package com.gruvedrift.config

import com.gruvedrift.Environment
import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import java.sql.Connection

/**
 *  Want to use object since it is a singleton in Kotlin.
 */
object DatabaseConfig {

    private lateinit var datasource: HikariDataSource

    fun init(environment: Environment = Environment.DEV) {
        val configurationParams = ConfigFactory.load().getConfig(environment.environmentName)

        val hikariConfig = HikariConfig().apply {
            driverClassName = configurationParams.getString("driver")
            jdbcUrl = configurationParams.getString("url")
            username = configurationParams.getString("username")
            password = configurationParams.getString("password")
            maximumPoolSize = 10

        }

        datasource = HikariDataSource(hikariConfig)

        Flyway.configure()
            .dataSource(datasource)
            .locations("classpath:db/migration")
            .load()
            .migrate()

        Database.connect(datasource)
    }
}


/**
 * Unlike Spring Boot, where a datasource if often injected or wired explicitly to a repository class,
 * MyRepository( private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate ) {
 *  // do logic on the jdbcTemplate like .query() and .update()
 * }
 * Exposed manages a Global Database Connection pool. Configured once, and used implicitly in every transaction {}
 * That means that one does not have to use the @Transactional on functions that often are misconfigured or does not work
 * when failing transactions are cross-application.
 * */
