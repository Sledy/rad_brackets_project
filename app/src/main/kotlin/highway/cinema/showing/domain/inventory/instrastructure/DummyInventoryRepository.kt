package highway.cinema.showing.domain.inventory.instrastructure

import highway.cinema.showing.domain.inventory.domain.InventoryRepository
import highway.cinema.showing.domain.inventory.domain.ItemType

internal class DummyInventoryRepository : InventoryRepository {
    override fun getNumberOfAvailableItems(itemType: ItemType): Long {
        return when (itemType) {
            ItemType.GLASSES -> 300
            else -> 1
        }
    }
}
