package spectrum.sexplugin.hardcore.services

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import spectrum.sexplugin.SexPlugin
import spectrum.sexplugin.core.Actor
import spectrum.sexplugin.hardcore.models.Statistics
import spectrum.sexplugin.hardcore.repos.MemoryStatisticsRepository
import spectrum.sexplugin.hardcore.repos.MongoStatisticsRepository

class StatisticsServiceActor: Actor<Msg>() {
    private val logger = SexPlugin.plugin.logger

    override fun createActor(): SendChannel<Msg> {
        return SexPlugin.scope.statisticsActor()
    }

    suspend fun kill(userId: String, reason: String): CompletableDeferred<Statistics?>? {
        if (!isActive) start()
        return actor?.let { actor ->
            val result = CompletableDeferred<Statistics?>()
            actor.send(Msg.Kill(userId, reason, result))
            return result
        }
    }

    suspend fun find(userId: String): CompletableDeferred<Statistics?>? {
        if (!isActive) start()
        return actor?.let { actor ->
            val result = CompletableDeferred<Statistics?>()
            actor.send(Msg.Find(userId, result))
            return result
        }
    }
}

sealed interface Msg {
    class Kill(val userId: String, val reason: String, val result: CompletableDeferred<Statistics?>): Msg
    class Find(val userId: String, val result: CompletableDeferred<Statistics?>): Msg
}

@OptIn(ObsoleteCoroutinesApi::class)
private fun CoroutineScope.statisticsActor() = actor<Msg> {
    val repo = if (SexPlugin.plugin.config.getString("hardcore-services-statistics-repo_type", "memory") == "memory")
             MemoryStatisticsRepository()
        else MongoStatisticsRepository(SexPlugin.plugin.db.getCollection("statistics", Statistics::class.java))
    val service = StatisticsService(repo)
    val logger = SexPlugin.plugin.logger

    logger.info("[Statistics Actor] start (used ${repo::class.simpleName} repo)")
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