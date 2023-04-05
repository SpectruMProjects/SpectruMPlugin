package spectrum.sexplugin.hardcore.commands

import com.mongodb.client.model.Filters.eq
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.litote.kmongo.findOne
import org.litote.kmongo.replaceOneById
import spectrum.sexplugin.hardcore.Mongo


class ResetTimeHandler : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
            val username = if (args.isEmpty()) sender.name else args[0]
            var user = Mongo.UserStatistics.findOne(eq("username", username))
            if(user == null)
            {
                sender.sendMessage("§cUser not found")
                return true
            }
            user = user.copy(timeOnServer = 0, lastServerTime = System.currentTimeMillis())
            Mongo.UserStatistics.replaceOneById(user._id, user)
            sender.sendMessage("Time On Server is cleared for $username")
            return true
    }
}