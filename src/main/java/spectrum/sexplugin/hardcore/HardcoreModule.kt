package spectrum.sexplugin.hardcore

import org.bukkit.plugin.Plugin
import spectrum.sexplugin.hardcore.models.DatabaseData

object HardcoreModule {
    private lateinit var plugin: Plugin
    fun init(plugin: Plugin){
        this.plugin = plugin
        val data = DatabaseData(
            plugin.config.getString("mongo-url")!!,
            plugin.config.getString("mongo-db")!!,
            "hardcore-stats"
        )
        Mongo.initMongoCollection(data)
    }
}