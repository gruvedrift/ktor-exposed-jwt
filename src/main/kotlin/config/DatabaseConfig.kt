package com.gruvedrift.config

import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import java.sql.Connection

/**
 *  Want to use object since it is a singleton in Kotlin.
 */
object DatabaseConfig {

    private lateinit var datasource: HikariDataSource

    fun init() {
        val configurationParams = ConfigFactory.load().getConfig("database")

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
    }
    fun getConnection(): Connection = datasource.connection
}
