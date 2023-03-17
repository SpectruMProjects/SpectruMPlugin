package spectrum.sexplugin.whitelist.services

import kotlinx.coroutines.runBlocking
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import spectrum.sexplugin.SexPlugin
import spectrum.sexplugin.whitelist.Api

class WhitelistService(
    private val api: Api
) {
    fun onPlayerJoin(player: Player) {
        runBlocking {
            if (!api.hasPlayerAccess(player.name)) {
                player.kick(Component.text("Сосать + лежать (зарегестрируйтесь на нешем сайте)"))
                SexPlugin.plugin.logger.info("${player.name} не прошёл")
            }
        }
    }
}