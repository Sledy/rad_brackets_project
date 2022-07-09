package highway.cinema.showing.domain.inventory.instrastructure

import highway.cinema.showing.domain.inventory.InventoryRepository
import highway.cinema.showing.domain.inventory.ItemType

internal class DummyInventoryRepository : InventoryRepository {
    override fun getNumberOfAvailableItems(itemType: ItemType): Long {
        return when (itemType) {
            ItemType.GLASSES -> 100
            else -> 1
        }
    }
}
