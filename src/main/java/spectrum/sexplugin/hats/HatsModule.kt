package spectrum.sexplugin.hats

import spectrum.sexplugin.Module
import spectrum.sexplugin.SexPlugin

object HatsModule : Module {
    private lateinit var plugin: SexPlugin
    override fun init(plugin: SexPlugin) {
        this.plugin = plugin
        plugin.registerEventListener(HatsListener())
    }
}