package highway.cinema.showing.domain.inventory.domain

interface InventoryRepository {

    fun getNumberOfAvailableItems(itemType: ItemType): Long

}
