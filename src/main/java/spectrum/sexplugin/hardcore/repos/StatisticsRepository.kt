package spectrum.sexplugin.hardcore.repos

import spectrum.sexplugin.hardcore.models.Statistics

interface StatisticsRepository {
    suspend fun find(userId: String): Statistics?
    suspend fun save(statistics: Statistics): Unit
}