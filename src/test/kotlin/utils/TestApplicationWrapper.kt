//package utils
//
//import com.gruvedrift.Environment
//import com.gruvedrift.module
//import io.ktor.client.*
//import io.ktor.client.plugins.contentnegotiation.*
//import io.ktor.serialization.kotlinx.json.*
//import io.ktor.server.testing.*
//
//fun runEngineTest(
//    environment: Environment = Environment.TEST,
//    testBlock: suspend (client: HttpClient) -> Unit
//) = testApplication {
//    application { module(environment = environment) }
//    val client = createClient {
//        install(ContentNegotiation) { json() }
//    }
//    testBlock(client)
//}