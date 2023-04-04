package spectrum.sexplugin.hardcore.commands

import com.mongodb.client.model.Filters.eq
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.litote.kmongo.findOne
import spectrum.sexplugin.hardcore.Mongo


class ShowStatsHandler : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
            val username = if (args.isEmpty()) sender.name else args[0]
            val user = Mongo.UserStatistics.findOne(eq("username", username))
            if(user == null)
            {
                sender.sendMessage("§cUser not found")
                return true
            }
            sender.sendMessage("Stats for $username")
            sender.sendMessage("Last Time On Server (Alive):${user.lastServerTime}")
            sender.sendMessage("Time On Server (Alive):${user.timeOnServer}")
            sender.sendMessage("Last respawn time:${user.stats.last().timeToRespawn}")
            sender.sendMessage("Last death issue:${user.stats.last().deathIssue}")
            return true
    }
}