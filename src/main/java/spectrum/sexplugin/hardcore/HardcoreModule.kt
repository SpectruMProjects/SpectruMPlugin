package spectrum.sexplugin.hardcore

import spectrum.sexplugin.SexPlugin
import spectrum.sexplugin.hardcore.models.Statistics
import spectrum.sexplugin.hardcore.repos.MemoryStatisticsRepository
import spectrum.sexplugin.hardcore.repos.MongoStatisticsRepository
import spectrum.sexplugin.hardcore.services.StatisticsService

object HardcoreModule {
    lateinit var service: StatisticsService
    private set

    fun init(plugin: SexPlugin) {
        val repo =
            if (SexPlugin.plugin.config.getString("hardcore-services-statistics-repo_type", "memory") == "memory")
                MemoryStatisticsRepository()
            else MongoStatisticsRepository(SexPlugin.plugin.db.getCollection("statistics", Statistics::class.java))
        service = StatisticsService(repo)
    }
}