package spectrum.sexplugin

import kotlinx.coroutines.MainCoroutineDispatcher
import org.bukkit.plugin.Plugin
import kotlin.coroutines.CoroutineContext

class MainThreadDispatcher(
    private val plugin: Plugin
): MainCoroutineDispatcher() {
    override val immediate: MainCoroutineDispatcher = this

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        task { block.run() }.runTask(plugin)
    }
}