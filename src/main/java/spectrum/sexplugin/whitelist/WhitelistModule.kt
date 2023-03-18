package spectrum.sexplugin.whitelist

import spectrum.sexplugin.SexPlugin
import spectrum.sexplugin.whitelist.listenrers.JoinListener
import spectrum.sexplugin.whitelist.services.WhitelistService

object WhitelistModule {
    fun init(plugin: SexPlugin) {
        val api = Api("http://localhost:5168")
        val service = if (SexPlugin.Config.getBoolean("whitelist-active", false))
                 WhitelistService.Api(api)
            else WhitelistService.Fake()
        plugin.logger.info("Take ${service::class.simpleName} whitelist service")
        val listener = JoinListener(service)

        plugin.registerEventListener(listener)
    }
}