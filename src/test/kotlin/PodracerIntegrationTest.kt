import com.gruvedrift.Environment
import com.gruvedrift.domain.dto.request.CreatePodracerRequest
import com.gruvedrift.domain.model.Podracer
import com.gruvedrift.exception.DomainEntity.PODRACER
import com.gruvedrift.module
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals
import org.junit.jupiter.api.assertAll

/*
*  This is one way to set up test in KTOR.
*  Each test spins up a fresh testApplication {} block per test.
* Why?
*   - Isolation: Each test runs its own embedded server in a clean test state.
*   - Custom setup: Configure different modules for each test if needed.
*   - testApplication {} spins up an internal test engine that is lightweight and fast, so not much overhead.
* */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PodracerIntegrationTest {

    @Test
    fun `get podracer by ID - success`() = testApplication {
        application {  module(environment = Environment.TEST) }
        client = createClient { install(ContentNegotiation) { json() } }
        val response = client.get("/api/podracer/1")
        assertEquals(HttpStatusCode.OK, response.status)
        val podracer: Podracer = response.body()
        assertAll(
            { assertEquals(1, podracer.id) },
            { assertEquals(1, podracer.engineId) },
            { assertEquals(1500, podracer.weight) },
            { assertEquals(947, podracer.maxSpeed) },
        )
    }

    @Test
    fun `get pod racer by ID - not found`() = testApplication {
        application { module(environment = Environment.TEST) }
        val client = createClient { install(ContentNegotiation) { json() } }
        val response = client.get("/api/podracer/666")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `get engine model - success`() = testApplication {
        application { module(environment = Environment.TEST) }
        val client = createClient { install(ContentNegotiation) { json() } }
        val response = client.get("/api/podracer/engine-model/1")
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("Engine model: 620C"))
    }

    @Test
    fun `create new podracer - success`() = testApplication {
        application { module(environment = Environment.TEST) }
        val client = createClient { install(ContentNegotiation) { json() } }
        val request = CreatePodracerRequest(
            engineId = 4,
            model = "ICX-12 Blacktail",
            manufacturer = "Hyperbaric Industries Inc.",
            weight = 1750,
            maxSpeed = 850,
        )
        val response = client.post("/api/podracer/create") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        assertEquals(HttpStatusCode.Created, response.status)
        assertTrue(response.bodyAsText().contains("Successfully created podracer with id:"))
    }

    @Test
    fun `delete podracer - invalid ID`() = testApplication {
        application { module(environment = Environment.TEST) }
        val client = createClient { install(ContentNegotiation) { json() } }
        val response = client.delete("/api/podracer/delete/invalid-id")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `delete podracer - non-existent ID`() = testApplication {
        application { module(environment = Environment.TEST) }
        val client = createClient { install(ContentNegotiation) { json() } }
        val response = client.delete("/api/podracer/delete/1337")
        assertEquals(HttpStatusCode.InternalServerError, response.status)
        assertTrue(response.bodyAsText().contains("Failed to delete entity: $PODRACER"))
    }

    @Test
    fun `delete podracer - success`() = testApplication {
        application { module(environment = Environment.TEST) }
        val client = createClient { install(ContentNegotiation) { json() } }
        val id = 6
        val deleteResponse = client.delete("/api/podracer/delete/$id")
        assertEquals(HttpStatusCode.OK, deleteResponse.status)
        assertTrue(deleteResponse.bodyAsText().contains("Successfully deleted podracer with id: $id"))
    }
}


