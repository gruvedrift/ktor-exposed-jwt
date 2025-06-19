val h2_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val postgres_version: String by project

plugins {
    kotlin("jvm") version "2.1.10"
    id("io.ktor.plugin") version "3.2.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.10"
}

group = "com.gruvedrift"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-auth")
    implementation("io.ktor:ktor-server-auth-jwt")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")

    implementation("org.postgresql:postgresql:$postgres_version")
    implementation("com.zaxxer:HikariCP:5.1.0") // JDBC connection pool
    implementation("org.flywaydb:flyway-core:10.10.0") // newer version for newer PSQL
    implementation("org.flywaydb:flyway-database-postgresql:10.10.0") // needed to actually run the flyway migration



    implementation("io.ktor:ktor-server-netty")
    implementation("ch.qos.logback:logback-classic:$logback_version")
}
