package spectrum.sexplugin.hardcore

import kotlinx.coroutines.launch
import spectrum.sexplugin.SexPlugin
import spectrum.sexplugin.hardcore.commands.StatisticsServiceRestartCommand
import spectrum.sexplugin.hardcore.models.Statistics
import spectrum.sexplugin.hardcore.repos.MongoStatisticsRepository
import spectrum.sexplugin.hardcore.services.StatisticsService
import spectrum.sexplugin.hardcore.services.StatisticsServiceActor

object HardcoreModule {
    lateinit var service: StatisticsServiceActor
    private set

    fun init(plugin: SexPlugin) {
        val repo = MongoStatisticsRepository(plugin.db.getCollection("statistics", Statistics::class.java))
        val service = StatisticsService(repo)
        HardcoreModule.service = StatisticsServiceActor(service)

        plugin.getCommand("hardcore-stats-service-restart")
            ?.setExecutor(StatisticsServiceRestartCommand(HardcoreModule.service))
    }
}