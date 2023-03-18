package spectrum.sexplugin.whitelist

import spectrum.sexplugin.SexPlugin
import spectrum.sexplugin.whitelist.listenrers.JoinListener
import spectrum.sexplugin.whitelist.services.WhitelistService

object WhitelistModule {
    fun init(plugin: SexPlugin) {
        val api = Api("localhost", 5168)
        val service = WhitelistService.Api(api)
        val listener = JoinListener(service)

        plugin.registerEventListener(listener)
    }
}