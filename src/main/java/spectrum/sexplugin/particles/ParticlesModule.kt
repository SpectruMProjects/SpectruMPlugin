package spectrum.sexplugin.particles

import spectrum.sexplugin.SexPlugin
import spectrum.sexplugin.particles.commands.OpenParticlesMenuCommand
import spectrum.sexplugin.particles.controllers.MainController

object ParticlesModule {
    fun onInit(plugin: SexPlugin) {
        registerRoutes(MainController())
        plugin.getCommand("sex-p-menu")?.setExecutor(OpenParticlesMenuCommand())
    }
}