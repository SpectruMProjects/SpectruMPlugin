package spectrum.sexplugin.hardcore

import com.mongodb.client.model.Filters.eq
import org.bson.types.ObjectId
import org.bukkit.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.litote.kmongo.findOne
import org.litote.kmongo.replaceOneById
import spectrum.sexplugin.SexPlugin
import spectrum.sexplugin.hardcore.models.Stat
import spectrum.sexplugin.hardcore.models.UserStatistics

class HardcoreListener : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent){
        var user = Mongo.UserStatistics.findOne(eq("username", event.player.name))
        if(user == null) {
            SexPlugin.plugin.logger.warning("${event.player.name} not found, creating")
            Mongo.UserStatistics.insertOne(
                UserStatistics(
                    ObjectId.get(),
                    event.player.name,
                    0,
                    System.currentTimeMillis(),
                    emptyList<Stat>().toMutableList()
                )
            )
        }else{
            user = updateLastServerTime(user)
            Mongo.UserStatistics.replaceOneById(user._id, user)
        }
    }
    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent)
    {
        SexPlugin.plugin.logger.warning("player dead ${event.player.name}")
        var user = Mongo.UserStatistics.findOne(eq("username",event.player.name))
        if(user == null)
        {
            event.player.gameMode = GameMode.SURVIVAL
            event.player.spawnAt(Location(Bukkit.getWorld(event.player.name),0.0,80.0, 0.0))
            return
        }
        user = updateTimeOnServer(user)
        user = updateLastServerTime(user)
        @Suppress("DEPRECATION")
        user.stats.add(Stat(user.timeOnServer / 2, event.deathMessage!!))
        Mongo.UserStatistics.replaceOneById(user._id, user)
    }
    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent)
    {
        var user = Mongo.UserStatistics.findOne(eq("username",event.player.name))
        if(event.player.gameMode == GameMode.SPECTATOR || event.player.isDead){
            user = updateLastServerTime(user!!)
            Mongo.UserStatistics.replaceOneById(user._id, user)
        }
        user = updateTimeOnServer(user!!)
        user = updateLastServerTime(user)
        Mongo.UserStatistics.replaceOneById(user._id, user)
    }
    private fun updateTimeOnServer(user: UserStatistics): UserStatistics
    {
        return user.copy(timeOnServer = user.timeOnServer + System.currentTimeMillis() - user.lastServerTime)
    }
    private fun updateLastServerTime(user: UserStatistics): UserStatistics
    {
        return user.copy(lastServerTime = System.currentTimeMillis())
    }
}