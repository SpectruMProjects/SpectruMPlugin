package spectrum.sexplugin.whitelist

import spectrum.sexplugin.SexPlugin
import spectrum.sexplugin.whitelist.listenrers.JoinListener
import spectrum.sexplugin.whitelist.services.WhitelistService

object WhitelistModule {
    fun init(plugin: SexPlugin) {
        val logger = plugin.getLogger()
        val url = plugin.config.getString("url")!!
        val api = Api(url)
        val service = WhitelistService.Api(api)
        val listener = JoinListener(service)
        if(!plugin.config.getBoolean("whitelist-active"))
            logger.warning("Whitelist disabled from config!")
        else logger.info("API URL is: $url")
        plugin.registerEventListener(listener)

    }
}