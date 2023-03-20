package spectrum.sexplugin.hardcore

import ch.qos.logback.classic.Logger
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.bson.BsonDocument
import org.bson.BsonInt64
import org.bson.conversions.Bson
import spectrum.sexplugin.SexPlugin

object HardcoreModule {
    lateinit var mongoClient: MongoClient
    fun init(plugin: SexPlugin) {
        val dbString = plugin.config.getString("mongodb-url")!!
        val dbname = plugin.config.getString("mongodb-name")!!
        val serverApi = ServerApi.builder().version(ServerApiVersion.V1).build()
        val settings = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(dbString))
            .serverApi(serverApi).build()

        mongoClient = MongoClients.create(settings)
        val database = mongoClient.getDatabase(dbname)

        val mongoLogger = java.util.logging.Logger.getLogger("org.mongodb.driver.client")
        mongoLogger.level = java.util.logging.Level.WARNING

        val command: Bson = BsonDocument("ping", BsonInt64(1))
        database.runCommand(command)
        plugin.getLogger().warning("mongodb successfully connected!")
    }
}