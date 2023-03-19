package spectrum.sexplugin.whitelist

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import spectrum.sexplugin.SexPlugin.Companion.plugin

class Api(
    backUrl: String
) {
    private val client = HttpClient(CIO) {
        defaultRequest {
            url(backUrl)
        }
    }

    suspend fun hasPlayerAccess(username: String): Boolean {
        if(!plugin.config.getBoolean("whitelist-active")) return true
        val response = client.get("/Hardcore/access/$username")
        return response.status.value == 200
    }
}