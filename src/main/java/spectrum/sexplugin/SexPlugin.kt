package spectrum.sexplugin

import kotlinx.coroutines.*
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import spectrum.sexplugin.menu.MenuModule
import spectrum.sexplugin.whitelist.WhitelistModule
import java.util.logging.Logger
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
        lateinit var Config: FileConfiguration
        private set
        lateinit var mainlogger: Logger
    }

    override fun onEnable() {
        plugin = this
        mainlogger = plugin.getLogger()
        MainDispatcher = MainThreadDispatcher(this)
        DefaultDispatcher = PluginDispatcher(this)
        mainScope = CoroutineScope(MainDispatcher) + SupervisorJob()
        defaultScope = CoroutineScope(DefaultDispatcher) + SupervisorJob()
        initConfig()
        registerEvents()
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
        Config = config
//        val file = File(dataFolder.name + File.pathSeparator + "config.yml")
//        if(!file.exists())
//        {
//            saveResource("config.yml", true)
//        }
//        Config = YamlConfiguration.loadConfiguration(file)
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