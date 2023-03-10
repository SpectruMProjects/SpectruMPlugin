package spectrum.sexplugin.menu

import net.kyori.adventure.text.TextComponent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.player.PlayerQuitEvent

class ClickListener(private val service: MenuService): Listener {
    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val title = event.view.title()
        if (title !is TextComponent) return

        event.isCancelled = service.onItemClick(
            event.currentItem,
            title.content(),
            event.inventory,
            event.whoClicked.name,
            event.click
        )
    }

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        service.onPlayerCloseInventory(event.player.name)
    }

    @EventHandler
    fun onInventoryOpen(event: InventoryOpenEvent) {
        service.onPlayerOpenInventory(event.inventory, event.player.name)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        service.onPlayerQuit(event.player.name)
    }
}
