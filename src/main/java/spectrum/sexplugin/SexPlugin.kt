package spectrum.sexplugin

import com.mongodb.client.MongoClients
import kotlinx.coroutines.*
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import spectrum.sexplugin.hardcore.HardcoreModule
import spectrum.sexplugin.menu.MenuModule
import spectrum.sexplugin.whitelist.WhitelistModule
import kotlin.coroutines.CoroutineContext

class SexPlugin : JavaPlugin() {
    companion object {
        lateinit var MainDispatcher: MainCoroutineDispatcher
        private set

        lateinit var AsyncDispatcher: CoroutineDispatcher
        private set

        lateinit var scope: CoroutineScope
        private set

        lateinit var plugin: SexPlugin
        private set
    }

    private val client by lazy {
        MongoClients.create(config.getString("connections-mongo-url", "mongodb://localhost") ?: "mongodb://localhost")
    }
    val db by lazy {
        client.getDatabase(config.getString("connections-mongo-database", "devdb") ?: "devdb")
    }

    override fun onEnable() {
        plugin = this
        MainDispatcher = MainThreadDispatcher(this)
        AsyncDispatcher = if (config.getBoolean("coroutines-useTasks", false))
             PluginDispatcher(this)
        else Dispatchers.Default

        scope = CoroutineScope(AsyncDispatcher) + SupervisorJob()
        registerEvents()
        init()
    }

    override fun onDisable() {
        scope.cancel()
        MainDispatcher.cancel()
        client.close()
        Thread.sleep(100)
    }

    fun registerEventListener(listener: Listener) {
        server.pluginManager.registerEvents(listener, this)
    }

    private fun registerEvents() {
        MenuModule.eventListeners.forEach(::registerEventListener)
    }

    private fun init() {
        WhitelistModule.init(this)
        HardcoreModule.init(this)
    }
}

inline fun task(crossinline block: () -> Unit) = object : BukkitRunnable() {
    override fun run() {
        block()
    }
}

class MainThreadDispatcher(
    private val plugin: Plugin
): MainCoroutineDispatcher() {
    override val immediate: MainCoroutineDispatcher = this

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        task { block.run() }.runTask(plugin)
    }

}
class PluginDispatcher(
    private val plugin: Plugin
): CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        task { block.run() }.runTaskAsynchronously(plugin)
    }
}