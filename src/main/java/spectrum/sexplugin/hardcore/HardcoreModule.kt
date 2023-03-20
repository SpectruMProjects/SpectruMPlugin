package spectrum.sexplugin.hardcore

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
import org.slf4j.LoggerFactory
import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext

object HardcoreModule {
    lateinit var mongoClient: MongoClient
    fun init(plugin: SexPlugin) {
        val mongoLogger = LoggerContext().getLogger(Logger.ROOT_LOGGER_NAME)
        mongoLogger.level = Level.OFF
        val dbString = plugin.config.getString("mongodb-url")!!
        val dbname = plugin.config.getString("mongodb-name")!!
        val serverApi = ServerApi.builder().version(ServerApiVersion.V1).build()
        val settings = MongoClientSettings.builder().applyConnectionString(ConnectionString(dbString)).serverApi(serverApi).build()
        mongoClient = MongoClients.create(settings)
        val database = mongoClient.getDatabase(dbname)
        val command: Bson = BsonDocument("ping", BsonInt64(1))
        database.runCommand(command)
        plugin.getLogger().warning("mongodb successfully connected!")
    }
}