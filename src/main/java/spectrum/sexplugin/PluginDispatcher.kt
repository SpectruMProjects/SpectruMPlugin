package spectrum.sexplugin

import kotlinx.coroutines.CoroutineDispatcher
import org.bukkit.plugin.Plugin
import kotlin.coroutines.CoroutineContext

class PluginDispatcher(
    private val plugin: Plugin
): CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        task { block.run() }.runTaskAsynchronously(plugin)
    }
}