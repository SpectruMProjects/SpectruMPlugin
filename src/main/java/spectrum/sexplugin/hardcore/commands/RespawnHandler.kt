package spectrum.sexplugin.hardcore.commands

import com.mongodb.client.model.Filters.eq
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.litote.kmongo.findOne
import org.litote.kmongo.replaceOneById
import org.litote.kmongo.updateOneById
import spectrum.sexplugin.hardcore.Mongo
import spectrum.sexplugin.hardcore.models.UserStatistics


class RespawnHandler : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val username = if (args.isEmpty()) sender.name else args[0]
        val user = Mongo.UserStatistics.findOne(eq("username", username))
        val player = Bukkit.getPlayer(username)!!
        if (user == null) {
            sender.sendMessage("§cUser not found")
            return true
        }
        if(user.stats.size < 1 || player.gameMode != GameMode.SPECTATOR){
            sender.sendMessage("§cDoesn't need to respawn")
        }
        val newstats = user.stats
        val newlast = newstats.last().copy(timeToRespawn = System.currentTimeMillis())
        newstats[newstats.size - 1] = newlast;
        val newuser = user.copy(stats = newstats)
        Mongo.UserStatistics.updateOneById(user._id, newuser)
        spawnPlayer(player, user)

        sender.sendMessage("$username respawned")
        return true
    }
    private fun spawnPlayer(player: Player, user: UserStatistics) {
        if(player.gameMode == GameMode.SPECTATOR) {
            player.teleport(Bukkit.getWorld("World")!!.spawnLocation)
            player.gameMode = GameMode.SURVIVAL
        }
        val newuser = user.copy(isRespawningNow = false)
        Mongo.UserStatistics.replaceOneById(user._id, newuser)
    }
}