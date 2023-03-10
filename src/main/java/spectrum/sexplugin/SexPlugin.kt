package spectrum.sexplugin

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import spectrum.sexplugin.menu.MenuModule
import spectrum.sexplugin.particles.ParticlesModule
import kotlin.concurrent.thread

class SexPlugin : JavaPlugin() {
    companion object {
        lateinit var coroutinesScope: CoroutineScope
        private set

        lateinit var plugin: SexPlugin
        private set
    }

    private var isRunning = false
    private val coroutinesThread = thread(start = false) {
        runBlocking {
            coroutinesScope = this
            launch {
                while (isRunning) { delay( 5000 )}
            }
            logger.info("Coroutines thread stop work")
        }
    }

    override fun onEnable() {
        isRunning = true
        coroutinesThread.start()
        logger.info("Start coroutines thread")
        plugin = this

        registerEvents()
        init()
    }

    override fun onDisable() {
        isRunning = false
        logger.info("Stop coroutines thread")
    }

    fun registerEventListener(listener: Listener) {
        server.pluginManager.registerEvents(listener, this)
    }

    private fun registerEvents() {
        MenuModule.eventListeners.forEach(::registerEventListener)
    }

    private fun init() {
        ParticlesModule.onInit(this)
    }
}
