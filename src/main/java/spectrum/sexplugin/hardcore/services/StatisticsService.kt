package spectrum.sexplugin.hardcore.services

import spectrum.sexplugin.hardcore.models.Death
import spectrum.sexplugin.hardcore.models.Statistics
import spectrum.sexplugin.hardcore.repos.StatisticsRepository
import java.util.*

class StatisticsService(
    private val repo: StatisticsRepository
) {
    suspend fun find(userId: String) = repo.find(userId)

    suspend fun kill(userId: String, reason: String) {
        val now = Date()
        val stats = repo.find(userId) ?: Statistics(userId)
        val new = stats.kill(reason)
        repo.save(new)
    }
}