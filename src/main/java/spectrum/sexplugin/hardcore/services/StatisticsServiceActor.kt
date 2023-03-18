package spectrum.sexplugin.hardcore.services

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import spectrum.sexplugin.SexPlugin
import spectrum.sexplugin.hardcore.models.Statistics

class StatisticsServiceActor(
    private val service: StatisticsService,
) {
    private val logger = SexPlugin.plugin.logger
    private var actor: SendChannel<Msg>? = null
    @OptIn(ExperimentalCoroutinesApi::class)
    val isActive
        get() = actor?.isClosedForSend == false

    init {
        start()
    }

    /**
     * Стартует актор если он ещё не был запущен
     */
    fun start() {
        if (isActive) return
        actor = SexPlugin.scope.statisticsActor(service)
    }

    /**
     * Останавливает актор
     */
    fun stop() {
        actor?.close()
        actor = null
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
    val logger = SexPlugin.plugin.logger

    logger.info("[Statistics Actor] start")
    try {
        for (msg in channel) {
            when(msg) {
                is Msg.Kill -> {
                    val stats = service.kill(msg.userId, msg.reason)
                    logger.info("[Statistics Actor] killed ${msg.userId} by ${msg.reason}")
                    msg.result.complete(stats)
                }
                is Msg.Find -> {
                    val stats = service.find(msg.userId)
                    msg.result.complete(stats)
                }
            }
        }
        logger.info("[Statistics Actor] stopped")
    } catch (e: CancellationException) {
        logger.info("[Statistics Actor] cancelled")
        throw e
    } catch (e: Exception) {
        logger.info("[Statistics Actor] stopped by $e")
        logger.warning(e.toString())
    }
}