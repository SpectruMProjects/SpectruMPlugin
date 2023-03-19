package spectrum.sexplugin.whitelist

import spectrum.sexplugin.SexPlugin
import spectrum.sexplugin.whitelist.listenrers.JoinListener
import spectrum.sexplugin.whitelist.services.WhitelistService

object WhitelistModule {
    fun init(plugin: SexPlugin) {
        val url = SexPlugin.Config.getString("url")!!
        val api = Api(url)
        val service = WhitelistService.Api(api)
        val listener = JoinListener(service)
        if(!SexPlugin.Config.getBoolean("whitelist-active"))
            SexPlugin.mainlogger.warning("Whitelist disabled from config!")
        else SexPlugin.mainlogger.info("API URL is: $url")
        plugin.registerEventListener(listener)
    }
}