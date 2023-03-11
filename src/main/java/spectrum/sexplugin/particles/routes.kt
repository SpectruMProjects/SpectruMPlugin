package spectrum.sexplugin.particles

import spectrum.sexplugin.menu.route
import spectrum.sexplugin.particles.controllers.MainController

fun registerRoutes(controller: MainController) {
    route("/particles/") {
        controller.index(player)
    }

    route("/ender-chest") {
        controller.ender(player)
    }
}