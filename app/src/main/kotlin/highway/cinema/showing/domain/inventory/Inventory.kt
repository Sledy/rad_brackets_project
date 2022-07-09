package highway.cinema.showing.domain.inventory

import highway.cinema.showing.domain.inventory.exception.InsufficientItemsInInventoryException

class Inventory(private val inventoryRepository: InventoryRepository) {

    fun reserveItems(itemType: ItemType, numberOfItemsToReserve: Int) {
        val availableItems: Long = inventoryRepository.getNumberOfAvailableItems(itemType)
        if (numberOfItemsToReserve > availableItems) {
            throw InsufficientItemsInInventoryException.from(itemType, numberOfItemsToReserve, availableItems)
        }
    }
}
