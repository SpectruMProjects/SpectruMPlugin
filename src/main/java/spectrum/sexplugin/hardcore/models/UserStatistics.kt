package spectrum.sexplugin.hardcore.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.time.LocalTime

data class UserStatistics(@BsonId val _id: ObjectId, val userid: String, val timeOnServer: LocalTime, val stats: MutableList<Stat>)
data class Stat(val timeToRespawn: LocalTime, val deathIssue: String)