package spectrum.sexplugin.hardcore

import spectrum.sexplugin.SexPlugin
import spectrum.sexplugin.hardcore.models.DatabaseData

object HardcoreModule {
    private lateinit var plugin: SexPlugin
    fun init(plugin: SexPlugin){
        this.plugin = plugin
        val data = DatabaseData(
            plugin.config.getString("mongo-url")!!,
            plugin.config.getString("mongo-db")!!,
            "hardcore-stats"
        )
        Mongo.initMongoCollection(data)
        plugin.registerEventListener(HardcoreListener())
    }
}