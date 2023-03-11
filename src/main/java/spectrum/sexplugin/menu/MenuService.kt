package spectrum.sexplugin.menu

import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import spectrum.sexplugin.SexPlugin
import java.net.URI

data class ClickContext(
    val uri: URI,
    val item: ItemStack,
    val player: Player,
    val inventory: Inventory,
    val clickType: ClickType
)

typealias ClickHandler = ClickContext.() -> Unit

object MenuService {
    private val playersWithOpenInventories = mutableMapOf<String, Inventory>()
    private val clickHandles = mutableMapOf<URI, ClickHandler>()

    fun onPlayerOpenInventory(inventory: Inventory, player: String) { playersWithOpenInventories[player] = inventory }
    fun onPlayerCloseInventory(player: String) { playersWithOpenInventories.remove(player) }
    fun onPlayerQuit(player: String) { onPlayerCloseInventory(player) }

    fun onItemClick(
        item: ItemStack?,
        inventoryName: String,
        inventory: Inventory,
        whoClicked: String,
        clickType: ClickType
    ): Boolean {
        if (inventoryName != "Меню") return false

        val player = Bukkit.getPlayer(whoClicked) ?: return false
        if (!playersWithOpenInventories.containsKey(whoClicked)) return false

        val itemURI = item?.query?.let { URI(it) }

        if (itemURI != null) {
            clickHandles.forEach { (uri, handler) ->
                if (uri.path != itemURI.path) return@forEach

                handler(ClickContext(itemURI, item, player, inventory, clickType))
                return true
            }
        }

        return false
    }

    fun addClickListener(query: String, handler: ClickHandler) {
        clickHandles[URI(query)] = handler
    }
    fun removeClickListener(handler: ClickHandler) {
        val inventoryName = clickHandles.toList().find { it.second == handler }?.first ?: return
        clickHandles.remove(inventoryName)
    }
}

val key = NamespacedKey(SexPlugin.plugin, "__query")
var ItemStack.query: String?
    get() = itemMeta.persistentDataContainer.get(key, PersistentDataType.STRING)
    set(value) {
        editMeta {
            val container = it.persistentDataContainer
            if (value == null) {
                container.remove(key)
                return@editMeta
            }
            container.set(key, PersistentDataType.STRING, value)
        }
    }

fun route(query: String, handler: ClickHandler) {
    MenuService.addClickListener(query, handler)
}