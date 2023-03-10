package spectrum.sexplugin.particles.screens

import org.bukkit.Material
import spectrum.sexplugin.menu.*

fun CreateMenuContext.particlesScreen() {
    item(Material.COMPASS, position(0)) {
        name("Партиклы")
        uri("/particles/")
    }
}