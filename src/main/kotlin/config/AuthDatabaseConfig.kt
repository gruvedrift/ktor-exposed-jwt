package com.gruvedrift.config

import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database

object AuthDatabaseConfig {

    fun init() {

        val configurationParams = ConfigFactory.load().getConfig("auth-database")
        val hikariConfig = HikariConfig().apply {
            driverClassName = configurationParams.getString("driver")
            jdbcUrl = configurationParams.getString("url")
            username = configurationParams.getString("username")
            password = configurationParams.getString("password")
            maximumPoolSize = 5
        }

        val datasource = HikariDataSource(hikariConfig)

        // Run Flyway DB migrations
        Flyway.configure()
            .dataSource(datasource)
            .locations("classpath:db/migration-auth")
            .load()
            .migrate()

        Database.connect(datasource)
    }
}