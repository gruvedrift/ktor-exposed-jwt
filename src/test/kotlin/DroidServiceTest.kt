import com.gruvedrift.config.DatabaseConfig
import com.gruvedrift.repository.DroidRepository
import com.gruvedrift.service.DroidService
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import kotlin.test.Test
import kotlin.test.assertEquals

// Single instance of the test class allows for non-static use of @BeforeAll
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DroidServiceTest {

    private lateinit var droidService: DroidService

    @BeforeAll
    fun setup() {
        DatabaseConfig.init(environment = "test-database")
        droidService = DroidService(DroidRepository())
    }

    @Test
    fun `getDroidById returns droid when droid is found`() {
        val droid = droidService.getDroidById(2)
        assertEquals(2, droid.id)
    }

}