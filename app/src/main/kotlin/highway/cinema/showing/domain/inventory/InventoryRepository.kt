package highway.cinema.showing.domain.inventory

interface InventoryRepository {

    fun getNumberOfAvailableItems(itemType: ItemType): Long

}
