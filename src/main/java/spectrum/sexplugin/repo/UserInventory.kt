package spectrum.sexplugin.repo

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class UserInventory(@BsonId val _id: ObjectId, val Inventory: Array<String>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserInventory

        return Inventory.contentEquals(other.Inventory)
    }

    override fun hashCode(): Int {
        return Inventory.contentHashCode()
    }
}