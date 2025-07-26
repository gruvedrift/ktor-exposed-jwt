package utils

import com.gruvedrift.Environment
import com.gruvedrift.config.DatabaseConfig
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance

// Single instance of the test class allows for non-static use of @BeforeAll
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class BaseTestConfiguration {

    @BeforeAll
    fun setupDatabase() { DatabaseConfig.init(Environment.TEST) }

    @AfterAll
    fun teardownDatabase() { DatabaseConfig.close() }
}