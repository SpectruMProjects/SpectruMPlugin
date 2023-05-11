package spectrum.sexplugin.repo

import com.mongodb.client.MongoCollection
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection

object Mongo {
    lateinit var UserStatistics: MongoCollection<UserStatistics>
        private set
    fun initMongoCollection(data: DatabaseData)
    {
        System.setProperty("org.litote.mongo.test.mapping.service", "org.litote.kmongo.jackson.JacksonClassMappingTypeService")
        val client = KMongo.createClient(data.connectionString)
        val db = client.getDatabase(data.database)
        UserStatistics = db.getCollection<UserStatistics>(data.statsCollection)
    }
}