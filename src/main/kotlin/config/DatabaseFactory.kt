package com.gruvedrift.config

import com.typesafe.config.Config
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.slf4j.LoggerFactory

fun initDatabase(
    dbConfig: Config,
    migrationLocation: String,
) : DatabaseContext {

    // Configure Hikari
    val hikariConfig = HikariConfig().apply {
        driverClassName = dbConfig.getString("driver")
        jdbcUrl = dbConfig.getString("url")
        username = dbConfig.getString("username")
        password = dbConfig.getString("password")
        maximumPoolSize = dbConfig.getInt("maximumPoolSize")
    }

    // Create datasource instance
    val datasource = HikariDataSource(hikariConfig)

    // Run migration
    Flyway.configure()
        .dataSource(datasource)
        .locations(migrationLocation)
        .load()
        .migrate()

    // Create database instance
    val database = Database.connect(datasource)
    return DatabaseContext(
        database = database,
        datasource = datasource
    )
}

private val logger = LoggerFactory.getLogger("StatusPages")

fun closeDatabase(databaseContext: DatabaseContext) {
    logger.info("Closing database connection for datasource: ${databaseContext.datasource.jdbcUrl} ...")
    databaseContext.datasource.close()
}

data class DatabaseContext(
    val database: Database,
    val datasource: HikariDataSource
)
