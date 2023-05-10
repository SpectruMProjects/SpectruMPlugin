package spectrum.sexplugin.hats

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class HatsListener : Listener {
    @EventHandler
    fun onInventoryClicked(event: InventoryClickEvent)
    {
        event.isCancelled = true
    }
}