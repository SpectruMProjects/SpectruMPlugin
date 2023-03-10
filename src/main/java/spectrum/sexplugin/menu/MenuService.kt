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


typealias ClickHandler = (uri: URI, item: ItemStack, player: Player, inventory: Inventory) -> Unit

object MenuService {
    private val playersWithOpenInventories = mutableMapOf<String, Inventory>()
    private val clickHandles = mutableMapOf<Pair<String, URI>, ClickHandler>()

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
        val player = Bukkit.getPlayer(whoClicked) ?: return false
        if (!playersWithOpenInventories.containsKey(whoClicked)) return false

        if (item?.query == null) return true

        clickHandles.forEach { (inventoryName_Query, handler) ->
            if (inventoryName.lowercase() != inventoryName_Query.first) return@forEach

            val itemURI = item.query?.let { URI(it) } ?: return@forEach
            if (inventoryName_Query.second.path != itemURI?.path) return@forEach

            handler(itemURI, item, player, inventory)
            return true
        }

        return true
    }

    fun addClickListener(inventoryName: String, query: String, handler: ClickHandler) {
        clickHandles[inventoryName.lowercase() to URI(query)] = handler
    }
    fun removeClickListener(inventoryName: String, query: String) {
        clickHandles.remove(inventoryName to URI(query))
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

fun route(inventoryName: String, query: String, handler: ClickHandler) {
    MenuService.addClickListener(inventoryName, query, handler)
}