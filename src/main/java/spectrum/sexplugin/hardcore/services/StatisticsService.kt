package spectrum.sexplugin.hardcore.services

import spectrum.sexplugin.hardcore.models.Statistics
import spectrum.sexplugin.hardcore.repos.StatisticsRepository

class StatisticsService(
    private val repo: StatisticsRepository
) {
    suspend fun find(userId: String) = repo.find(userId)

    suspend fun kill(userId: String, reason: String): Statistics? {
        val stats = repo.find(userId) ?: return null
        val new = stats.kill(reason)
        repo.save(new)
        return new
    }
}