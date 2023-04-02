package spectrum.sexplugin.hardcore

import org.bson.types.ObjectId
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import spectrum.sexplugin.hardcore.models.Stat
import spectrum.sexplugin.hardcore.models.UserStatistics
import java.sql.Time
import java.time.LocalTime

class HardcoreListener : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent){
        Mongo.UserStatistics.insertOne(
            UserStatistics(
                ObjectId.get(),
                event.player.name,
                LocalTime.MIN,
                emptyList<Stat>().toMutableList()))
    }
}