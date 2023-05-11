package spectrum.sexplugin

import kotlinx.coroutines.*
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import spectrum.sexplugin.hardcore.HardcoreModule
import spectrum.sexplugin.hats.HatsModule
import spectrum.sexplugin.repo.DatabaseData
import spectrum.sexplugin.repo.Mongo
import spectrum.sexplugin.whitelist.WhitelistModule

class SexPlugin : JavaPlugin() {
    companion object {
        lateinit var MainDispatcher: MainCoroutineDispatcher
        private set
        lateinit var DefaultDispatcher: CoroutineDispatcher
        private set
        lateinit var mainScope: CoroutineScope
        private set
        lateinit var defaultScope: CoroutineScope
        private set
        lateinit var plugin: JavaPlugin
        private set
    }

    override fun onEnable() {
        plugin = this
        MainDispatcher = MainThreadDispatcher(this)
        DefaultDispatcher = PluginDispatcher(this)
        mainScope = CoroutineScope(MainDispatcher) + SupervisorJob()
        defaultScope = CoroutineScope(DefaultDispatcher) + SupervisorJob()
        initConfig()
        init()
    }

    override fun onDisable() {
        MainDispatcher.cancel()
        DefaultDispatcher.cancel()
        Thread.sleep(100)
    }

    private fun initConfig()
    {
        saveDefaultConfig()
    }

    fun registerEventListener(listener: Listener) {
        server.pluginManager.registerEvents(listener, this)
    }

    private fun init() {
        WhitelistModule.init(this)
        HardcoreModule.init(this)
        HatsModule.init(this)
        val data = DatabaseData(
            plugin.config.getString("mongo-url")!!,
            plugin.config.getString("mongo-db")!!,
            "hardcore-stats",
            "users"
        )
        Mongo.initMongoCollection(data)
    }
}

inline fun task(crossinline block: () -> Unit) = object : BukkitRunnable() {
    override fun run() {
        block()
    }
}