package utils

import com.gruvedrift.Environment
import com.gruvedrift.config.DatabaseContext
import com.gruvedrift.config.closeDatabase
import com.gruvedrift.config.initDatabase
import com.typesafe.config.ConfigFactory
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance

/**
 * Base test configuration used to initialize and manage the lifecycle of required database contexts.
 * This setup supports multiple data sources,
 *
 * Design Rationale:
 * - Use `lateinit var` with `@TestInstance(TestInstance.Lifecycle.PER_CLASS)` to allow initialization
 *   of shared resources once per test class. This reduces test runtime overhead and avoids repeated
 *   database migrations per test method.
 *
 * - By centralizing `setupDatabase()` and `teardownDatabase()` in a base class, we enforce consistent
 *   initialization and cleanup logic across all integration tests that depend on database state.
 *
 * Tradeoffs:
 * - The mutable state (`lateinit`) introduces the risk of uninitialized access if lifecycle is not respected.
 * - This design assumes sequential or well-isolated test execution. Tests relying on shared mutable state
 *   should avoid parallel execution unless databases are containerized per test.
 *
 * If per-test isolation is ever required (e.g., for parallel test execution or stricter test purity),
 * this approach should be replaced with a per-method setup using `@BeforeEach` and ephemeral database containers.
 * Doing this would require a tear-down in an `@AfterAll` in order to avoid connection pool exhaustion.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class BaseTestConfiguration {

    lateinit var podracingDatabase: DatabaseContext
    lateinit var authenticationDatabase: DatabaseContext

    @BeforeAll
    fun setupDatabase() {
        val config = ConfigFactory.load().getConfig(Environment.TEST.configKey)

        podracingDatabase = initDatabase(
            dbConfig = config.getConfig("podracing"),
            migrationLocation = "classpath:db/migration"
        )
        authenticationDatabase = initDatabase(
            dbConfig = config.getConfig("auth"),
            migrationLocation = "classpath:db/migration-auth"
        )
    }

    @AfterAll
    fun teardownDatabase() {
        closeDatabase(podracingDatabase)
        closeDatabase(authenticationDatabase)
    }
}