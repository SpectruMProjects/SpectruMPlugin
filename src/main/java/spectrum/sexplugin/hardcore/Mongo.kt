package spectrum.sexplugin.hardcore

import org.litote.kmongo.KMongo
import spectrum.sexplugin.hardcore.models.DatabaseData

object Mongo {
    fun initMongoCollection(data: DatabaseData)
    {
        val client = KMongo.createClient(data.connectionString)
        val db = client.getDatabase(data.database)
        val collection = db.getCollection(data.collection)
    }
}