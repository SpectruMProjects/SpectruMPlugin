package spectrum.sexplugin.whitelist.listenrers

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import spectrum.sexplugin.whitelist.services.WhitelistService

class JoinListener(
    private val service: WhitelistService
): Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        service.onPlayerJoin(event.player)
    }
}