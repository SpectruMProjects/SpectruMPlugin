package spectrum.sexplugin.hardcore.services

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import spectrum.sexplugin.SexPlugin
import spectrum.sexplugin.hardcore.models.Statistics
import kotlin.random.Random

class StatisticsServiceActor(
    private val service: StatisticsService,
) {
    private val logger = SexPlugin.plugin.logger
    private var actor: SendChannel<Msg>? = null
    @OptIn(ExperimentalCoroutinesApi::class)
    val isActive
        get() = actor?.isClosedForSend == true

    init {
        start()
    }

    /**
     * Стартует актор если он ещё не был запущен
     */
    fun start() {
        if (isActive) return
        actor = SexPlugin.scope.statisticsActor(service)
        logger.info("${this.javaClass.simpleName} starting...")
    }

    /**
     * Останавливает актор
     */
    fun stop() {
        actor?.close()
        actor = null
        logger.info("${this.javaClass.simpleName} stopped")
    }

    /**
     * Перезапскает актор, если он был не запущен, то просто запускает
     */
    fun restart() {
        if (isActive) stop()
        start()
    }

    suspend fun kill(userId: String, reason: String): CompletableDeferred<Statistics?>? {
        return actor?.let { actor ->
            val result = CompletableDeferred<Statistics?>()
            actor.send(Msg.Kill(userId, reason, result))
            return result
        }
    }

    suspend fun find(userId: String): CompletableDeferred<Statistics?>? {
        return actor?.let { actor ->
            val result = CompletableDeferred<Statistics?>()
            actor.send(Msg.Find(userId, result))
            return result
        }
    }
}

private sealed interface Msg {
    class Kill(val userId: String, val reason: String, val result: CompletableDeferred<Statistics?>): Msg
    class Find(val userId: String, val result: CompletableDeferred<Statistics?>): Msg
}

@OptIn(ObsoleteCoroutinesApi::class)
private fun CoroutineScope.statisticsActor(service: StatisticsService) = actor<Msg> {
    try {
        for (msg in channel) {
            when(msg) {
                is Msg.Kill -> {
                    val stats = service.kill(msg.userId, msg.reason)
                    msg.result.complete(stats)
                }
                is Msg.Find -> {
                    val stats = service.find(msg.userId)
                    msg.result.complete(stats)
                }
            }
        }
    } catch (e: CancellationException) {
        SexPlugin.plugin.logger.info("Statistics actor cancelled")
        throw e
    } catch (e: Exception) {
        SexPlugin.plugin.logger.warning(e.toString())
    }
}