package spectrum.sexplugin.menu

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import kotlin.math.ceil

enum class InventorySize(val width: Int, val height: Int, val size: Int) {
    CHEST(9, 3, 27),
    LARGE_CHEST(9, 6, 54),
    HOPPER(9,  1, 9),
}
data class Position constructor(
    val x: Int,
    val y: Int,
    val position: Int
)
fun CreateMenuContext.position(position: Int): Position {
    val y = ceil((position.toDouble() / size.width - 1).coerceAtLeast(0.0)).toInt()
    return Position(
        (position - size.width * y - 1).coerceAtLeast(0),
        y,
        position
    )
}
fun CreateMenuContext.position(x: Int, y: Int) = Position(x, y, (y * size.width) + x)
fun CreateItemContext.position(position: Int): Position {
    val y = ceil((position.toDouble() / size.width - 1).coerceAtLeast(0.0)).toInt()
    return Position(
        (position - size.width * y - 1).coerceAtLeast(0),
        y,
        position
    )
}
fun CreateItemContext.position(x: Int, y: Int) = Position(x, y, (y * size.width) + x)



class CreateMenuContext(val inventory: Inventory, val size: InventorySize) {}
fun createMenu(
    size: InventorySize = InventorySize.LARGE_CHEST,
    builder: CreateMenuContext.() -> Unit
): Inventory {
    return Bukkit.createInventory(null, size.size, Component.text("Меню")).apply {
        builder(CreateMenuContext(this, size))
    }
}

class CreateItemContext(val inventory: Inventory, val item: ItemStack, val size: InventorySize) {}

fun CreateMenuContext.item(material: Material, pos: Position, amount: Int = 1, builder: CreateItemContext.() -> Unit) {
    val item = ItemStack(material, amount)
    builder(CreateItemContext(inventory, item, size))
    inventory.setItem(pos.position, item)
}
fun CreateMenuContext.item(pos: Position) {
    inventory.setItem(pos.position, null)
}

fun CreateItemContext.name(name: String) {
    item.editMeta {
        it.displayName(Component.text(name))
    }
}
fun CreateItemContext.name(name: Component) {
    item.editMeta {
        it.displayName(name)
    }
}

fun CreateItemContext.uri(uri: String) {
    item.query = uri
}