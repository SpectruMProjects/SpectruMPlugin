package spectrum.sexplugin.particles.controllers

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.entity.Player
import spectrum.sexplugin.SexPlugin

class MainController {
    fun index(player: Player) {
        player.sendMessage("Particles!")
        SexPlugin.coroutineScope.launch {
            delay(500)
            throw Exception("kek")
        }
        SexPlugin.coroutineScope.launch {
            delay(1000)
            println("Yup")
        }
    }
}