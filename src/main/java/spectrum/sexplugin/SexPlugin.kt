package spectrum.sexplugin

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.bukkit.plugin.java.JavaPlugin
import kotlin.concurrent.thread

class SexPlugin : JavaPlugin() {
    companion object {
        lateinit var coroutinesScope: CoroutineScope
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
    }

    override fun onDisable() {
        isRunning = false
        logger.info("Stop coroutines thread")
    }
}
