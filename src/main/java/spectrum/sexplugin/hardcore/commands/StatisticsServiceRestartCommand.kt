package spectrum.sexplugin.hardcore.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import spectrum.sexplugin.hardcore.services.StatisticsServiceActor

class StatisticsServiceRestartCommand(
    private val service: StatisticsServiceActor
): CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        name: String,
        args: Array<out String>?
    ): Boolean {
        if (!(sender is Player || sender is ConsoleCommandSender)) return true

        sender.sendMessage("Restarting statistics service...")
        service.restart()
        sender.sendMessage("Restarted statistics service.")

        return true
    }
}