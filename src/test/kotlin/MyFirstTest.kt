//import com.gruvedrift.domain.dto.response.PodracerAnalyticsCoreData
//import com.gruvedrift.domain.dto.response.PodracerAnalyticsResponse
//import com.gruvedrift.domain.dto.response.toAnalyticsResponse
//import com.gruvedrift.module
//import io.ktor.client.request.*
//import io.ktor.server.testing.*
//import org.junit.jupiter.api.Test
//import kotlin.test.assertEquals
//
//class MyFirstTest {
//
//    @Test
//    fun test() = testApplication {
//        application {
//            module()
//        }
//        val response = client.get("/api/engine/1")
//        println(response.call.response)
//    }
//
//    @Test
//    fun `test analytics`() {
//        assertEquals(expectedAnalyticsResponse, mockedPodracerAnalyticsCoreData.toAnalyticsResponse())
//
//    }
//
//}
//
//
//private val mockedPodracerAnalyticsCoreData = PodracerAnalyticsCoreData(
//    weight = 1000,
//    maxSpeed = 750,
//    engineEffectOutput = 800,
//    pilotName = "Sebulba",
//)
//
//private val expectedAnalyticsResponse = PodracerAnalyticsResponse(
//    powerToWeightRatio = 0.81,
//    efficiencyIndex = 703.13,
//    momentumPotential = 750_000,
//    overallPerformanceScore = 5.3,
//    flamethrower = true,
//)
//
