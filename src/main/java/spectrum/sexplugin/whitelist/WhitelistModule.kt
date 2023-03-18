package spectrum.sexplugin.whitelist

import spectrum.sexplugin.SexPlugin
import spectrum.sexplugin.whitelist.listenrers.JoinListener
import spectrum.sexplugin.whitelist.services.WhitelistService

object WhitelistModule {
    fun init(plugin: SexPlugin) {
        val api = Api(SexPlugin.Config.getString("url")!!)
        val service = WhitelistService.Fake()
        val listener = JoinListener(service)

        plugin.registerEventListener(listener)
    }
}