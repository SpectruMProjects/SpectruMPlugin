package spectrum.sexplugin.particles.controllers

import org.bukkit.entity.Player

class MainController {
    fun index(player: Player) {
        player.sendMessage("Particles!")
    }

    fun ender(player: Player) {
        player.openInventory(player.enderChest)
    }
}