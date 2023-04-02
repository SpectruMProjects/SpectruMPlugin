package spectrum.sexplugin.hardcore.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.time.LocalTime

data class UserStatistics(@BsonId val _id: ObjectId, val username: String, val timeOnServer: Long, val lastServerTime: Long, val stats: MutableList<Stat>)
data class Stat(val timeToRespawn: Long, val deathIssue: String)