package spectrum.sexplugin.hats

import com.mongodb.client.model.Filters.eq
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import spectrum.sexplugin.SexPlugin
import spectrum.sexplugin.repo.Mongo

class API {
    companion object{
        val HatsInventoryTitle = Component.text("Шапки")
    }
    fun createInventory(username: String): Inventory{
        val items = getItems(username)
        return Bukkit.createInventory(null, InventoryType.CHEST, HatsInventoryTitle).apply {
            var count = 0
            items.forEach {
                val item = ItemStack(Material.ICE, 1)
                item.editMeta { base ->
                    base.displayName(Component.text(it))
                }
                SexPlugin.plugin.logger.warning(item.itemMeta.toString())
                setItem(count++, item)
            }
        }
    }
    private fun getItems(username: String): Array<String>{
        val userinv = Mongo.UserInventory.find(eq("_username",username.lowercase()))
        return userinv.first()!!.Inventory
    }
}