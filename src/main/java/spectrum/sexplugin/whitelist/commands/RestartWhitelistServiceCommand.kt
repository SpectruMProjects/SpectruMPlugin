package spectrum.sexplugin.whitelist.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import spectrum.sexplugin.whitelist.actors.WhiteListServiceActor

class RestartWhitelistServiceCommand(
    private val service: WhiteListServiceActor
): CommandExecutor {
    override fun onCommand(sender: CommandSender, c: Command, n: String, a: Array<out String>?): Boolean {

        if (!(sender is Player || sender is ConsoleCommandSender)) return true

        sender.sendMessage("Restarting statistics service...")
        service.restart()
        sender.sendMessage("Restarted statistics service.")

        return true
    }
}