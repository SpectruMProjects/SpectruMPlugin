package spectrum.sexplugin.hardcore

import org.bukkit.Bukkit
import org.bukkit.configuration.ConfigurationSection
import spectrum.sexplugin.Module
import spectrum.sexplugin.SexPlugin
import spectrum.sexplugin.hardcore.commands.ResetTimeHandler
import spectrum.sexplugin.hardcore.commands.RespawnHandler
import spectrum.sexplugin.hardcore.commands.ShowStatsHandler

object HardcoreModule : Module {
    private lateinit var plugin: SexPlugin
    lateinit var hardcoreConfig: ConfigurationSection
    override fun init(plugin: SexPlugin){
        this.plugin = plugin
        hardcoreConfig = plugin.config.getConfigurationSection("hardcore")!!
        if(!Bukkit.isHardcore()) {
            plugin.logger.warning("Server is not on hardcore mode! Hardcore module will be disabled")
            return
        }
        plugin.registerEventListener(HardcoreListener())
        plugin.getCommand("resettime")!!.setExecutor(ResetTimeHandler())
        plugin.getCommand("showstats")!!.setExecutor(ShowStatsHandler())
        plugin.getCommand("respawn")!!.setExecutor(RespawnHandler())
    }
}