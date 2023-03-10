package spectrum.sexplugin.particles

import spectrum.sexplugin.menu.route
import spectrum.sexplugin.particles.controllers.MainController

fun registerRoutes(controller: MainController) {
    route("Партиклы", "/particles/") { uri, _, player, _ ->
        controller.index(player)
    }
}