package spectrum.sexplugin.hardcore.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class UserStatistics(@BsonId val _id: ObjectId, val username: String, val timeOnServer: Long, val lastServerTime: Long, val stats: MutableList<Stat>, val isRespawningNow: Boolean)
data class Stat(val timeToRespawn: Long, val deathTime: Long, val deathIssue: String, val deathIssuer: String)