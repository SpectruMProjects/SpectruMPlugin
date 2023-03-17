package spectrum.sexplugin.whitelist

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*

class Api(
    backHost: String,
    backPort: Int
) {
    private val client = HttpClient(CIO) {
        defaultRequest {
            host = backHost
            port = backPort
        }
    }

    suspend fun hasPlayerAccess(username: String): Boolean {
        val response = client.get("/Hardcore/access/$username")
        return response.status.value == 200
    }
}