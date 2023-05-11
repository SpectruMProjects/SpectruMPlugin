package spectrum.sexplugin.hats.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import spectrum.sexplugin.hats.API

class OpenHatsHandler : CommandExecutor {
    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>?): Boolean {
        val inv = API().createInventory(p0.name)
        (p0 as Player).openInventory(inv)
        return true
    }
}