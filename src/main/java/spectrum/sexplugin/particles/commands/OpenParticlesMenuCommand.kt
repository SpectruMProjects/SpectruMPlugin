package spectrum.sexplugin.particles.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import spectrum.sexplugin.menu.createMenu
import spectrum.sexplugin.particles.screens.particlesScreen

class OpenParticlesMenuCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) return true

        sender.openInventory(createMenu("Партиклы") {
          particlesScreen()
        })

        return true
    }
}