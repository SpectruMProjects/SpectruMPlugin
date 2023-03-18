package spectrum.sexplugin.hardcore.repos

import spectrum.sexplugin.hardcore.models.Statistics

class MemoryStatisticsRepository: StatisticsRepository {
    private val map = mutableMapOf<String, Statistics>()

    override suspend fun find(userId: String)= map[userId]
    override suspend fun save(statistics: Statistics) {
        map[statistics.userId] = statistics
    }
}