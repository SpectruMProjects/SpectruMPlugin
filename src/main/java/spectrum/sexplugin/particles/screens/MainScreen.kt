package spectrum.sexplugin.particles.screens

import org.bukkit.Material
import spectrum.sexplugin.menu.*

fun CreateMenuContext.particlesScreen() {
    item(Material.COMPASS, position(0)) {
        name("Партиклы")
        uri("/particles/")
    }

    item(Material.ENDER_CHEST, position(1)) {
        uri("/ender-chest")
    }
}