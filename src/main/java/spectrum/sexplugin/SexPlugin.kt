package spectrum.sexplugin

import kotlinx.coroutines.*
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import spectrum.sexplugin.hardcore.HardcoreModule
import spectrum.sexplugin.whitelist.WhitelistModule
import kotlin.coroutines.CoroutineContext

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
        HardcoreModule.mongoClient.close()
        Thread.sleep(50)
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