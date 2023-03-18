package spectrum.sexplugin

import kotlinx.coroutines.*
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginLogger
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import spectrum.sexplugin.menu.MenuModule
import spectrum.sexplugin.whitelist.WhitelistModule
import kotlin.coroutines.CoroutineContext

class SexPlugin : JavaPlugin() {
    companion object {
        lateinit var MainDispatcher: MainCoroutineDispatcher
        private set
        lateinit var Config: FileConfiguration;
        lateinit var DefaultDispatcher: CoroutineDispatcher
        private set

        lateinit var mainScope: CoroutineScope
        private set

        lateinit var defaultScope: CoroutineScope
        private set

        lateinit var plugin: SexPlugin
        private set

        lateinit var logger: PluginLogger
        private set
    }

    override fun onEnable() {
       // this.config.addDefault("whitelist-active", true)
        this.saveDefaultConfig()
        Config = this.config
        plugin = this
        MainDispatcher = MainThreadDispatcher(this)
        DefaultDispatcher = PluginDispatcher(this)
        mainScope = CoroutineScope(MainDispatcher) + SupervisorJob()
        defaultScope = CoroutineScope(DefaultDispatcher) + SupervisorJob()
        registerEvents()
        init()
    }

    override fun onDisable() {
        MainDispatcher.cancel()
        DefaultDispatcher.cancel()
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