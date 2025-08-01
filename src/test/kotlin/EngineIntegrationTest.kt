import com.gruvedrift.domain.dto.request.CreateEngineRequest
import com.gruvedrift.domain.dto.request.UpdateEngineEffectRequest
import com.gruvedrift.domain.model.Engine
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import utils.BaseTestConfiguration
import utils.runEngineTest
import kotlin.test.assertEquals

/**
 * Another way of structuring test is to use a shared setup and
 * simply wrap each testApplication engine initialization through a wrapper function.
 * */
class EngineIntegrationTest: BaseTestConfiguration() {

    @Test
    fun `get engine by ID - success`() = runEngineTest { client ->
        val response = client.get("/api/engine/2")
        assertEquals(HttpStatusCode.OK, response.status)
        val engine: Engine = response.body()
        assertAll(
            { assertEquals(2, engine.id) },
            { assertEquals(600, engine.effectOutput) },
            { assertEquals("Plug-F Mammoth Split-X", engine.model) },
        )
    }

    @Test
    fun `get engine by ID - invalid ID returns BadRequest`() = runEngineTest { client ->
        val response = client.get("/api/engine/invalid")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `create engine - success`() = runEngineTest { client ->
        val createRequest = CreateEngineRequest(
            manufacturer = "Subsonic Repulsion Tech",
            model = "AMX-1250",
            effectOutput = 1250,
        )

        val response = client.post("/api/engine/create") {
            contentType(ContentType.Application.Json)
            setBody(createRequest)
        }

        assertEquals(HttpStatusCode.Created, response.status)
        assertTrue(response.bodyAsText().contains("Created engine successfully with id"))
    }

    @Test
    fun `update engine effect - success`() = runEngineTest { client ->
        val updateRequest = UpdateEngineEffectRequest(
            id = 1,
            effectOutput = 2000
        )

        val response = client.put("/api/engine/update-effect") {
            contentType(ContentType.Application.Json)
            setBody(updateRequest)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Updated engine with id 1 to output 2000 Megawatt!", response.bodyAsText())
    }

    @Test
    fun `delete engine - success`() = runEngineTest { client ->
        val response = client.delete("/api/engine/delete/5")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Successfully deleted engine with id: 5", response.bodyAsText())
    }

    @Test
    fun `delete engine - invalid ID returns BadRequest`() = runEngineTest { client ->
        val response = client.delete("/api/engine/delete/invalid")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

}