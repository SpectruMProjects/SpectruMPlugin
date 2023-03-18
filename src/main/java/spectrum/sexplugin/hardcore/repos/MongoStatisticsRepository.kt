package spectrum.sexplugin.hardcore.repos

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters.eq
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import spectrum.sexplugin.SexPlugin
import spectrum.sexplugin.hardcore.models.Statistics

class MongoStatisticsRepository(
    private val collection: MongoCollection<Statistics>
): StatisticsRepository {
    override suspend fun find(userId: String): Statistics? {
        return SexPlugin.asyncScope.async(Dispatchers.IO) {
            collection.find(eq("userId", userId)).first()
        }.await()
    }

    override suspend fun save(statistics: Statistics) {
        SexPlugin.asyncScope.launch(Dispatchers.IO) {
            val current = collection.findOneAndReplace(
                eq("userId", statistics.userId),
                statistics
            )
            if (current == null) {
                collection.insertOne(statistics)
            }
        }
    }
}