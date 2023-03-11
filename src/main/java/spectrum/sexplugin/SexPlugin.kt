package spectrum.sexplugin

import kotlinx.coroutines.*
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import spectrum.sexplugin.menu.MenuModule
import spectrum.sexplugin.particles.ParticlesModule
import java.util.concurrent.Executors
import kotlin.time.Duration

class SexPlugin : JavaPlugin() {
    companion object {
        private val dispatcher = Executors.newCachedThreadPool().asCoroutineDispatcher()
        private lateinit var mainScope: CoroutineScope

        lateinit var coroutineScope: CoroutineScope
        private set

        lateinit var plugin: SexPlugin
        private set
    }

    override fun onEnable() {
        plugin = this
        CoroutineScope(dispatcher).launch {
            mainScope = this
            supervisorScope {
                coroutineScope = this@supervisorScope
                delay(Duration.INFINITE)
            }
        }

        registerEvents()
        init()
    }

    override fun onDisable() {
        mainScope.cancel()
        dispatcher.cancel()
        dispatcher.close()
        Thread.sleep(100)
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
