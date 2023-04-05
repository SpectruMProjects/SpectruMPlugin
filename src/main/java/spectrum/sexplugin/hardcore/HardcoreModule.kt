package spectrum.sexplugin.hardcore

import org.bukkit.Bukkit
import org.bukkit.configuration.ConfigurationSection
import spectrum.sexplugin.SexPlugin
import spectrum.sexplugin.hardcore.commands.ResetTimeHandler
import spectrum.sexplugin.hardcore.commands.RespawnHandler
import spectrum.sexplugin.hardcore.commands.ShowStatsHandler
import spectrum.sexplugin.hardcore.models.DatabaseData

object HardcoreModule {
    private lateinit var plugin: SexPlugin
    lateinit var hardcoreConfig: ConfigurationSection
    fun init(plugin: SexPlugin){
        this.plugin = plugin
        val data = DatabaseData(
            plugin.config.getString("mongo-url")!!,
            plugin.config.getString("mongo-db")!!,
            "hardcore-stats"
        )
        hardcoreConfig = plugin.config.getConfigurationSection("hardcore")!!
        if(!Bukkit.isHardcore()) {
            plugin.logger.warning("Server is not on hardcore mode! Hardcore module will be disabled")
            return
        }
        Mongo.initMongoCollection(data)
        plugin.registerEventListener(HardcoreListener())
        plugin.getCommand("resettime")!!.setExecutor(ResetTimeHandler())
        plugin.getCommand("showstats")!!.setExecutor(ShowStatsHandler())
        plugin.getCommand("respawn")!!.setExecutor(RespawnHandler())
    }
}