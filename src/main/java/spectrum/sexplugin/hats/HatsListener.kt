package spectrum.sexplugin.hats

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType

class HatsListener : Listener {
    @EventHandler
    fun onInventoryClicked(event: InventoryClickEvent)
    {
        //SexPlugin.plugin.logger.warning(event.view.title().toString())
        if(event.view.title() == API.HatsInventoryTitle && event.clickedInventory!!.type == InventoryType.CHEST) {
            event.whoClicked.inventory.addItem(event.currentItem!!)
            event.isCancelled = true
            event.whoClicked.closeInventory()
            //Mongo.UserInventory.find("custommodeldata", event.currentItem.itemMeta.customModelData)
        }
    }
}