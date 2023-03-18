package spectrum.sexplugin.whitelist

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import spectrum.sexplugin.SexPlugin

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
        return if(SexPlugin.Config.getBoolean("whitelist-active")) {
            response.status.value == 200
        } else true
    }
}