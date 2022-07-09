package highway.cinema.showing.domain.inventory.exception

import highway.cinema.showing.domain.inventory.ItemType

class InsufficientItemsInInventoryException private constructor(message: String?) : RuntimeException(message) {

    companion object {
        fun from(
            itemType: ItemType,
            requestedAmount: Int,
            itemsInInventory: Long
        ): InsufficientItemsInInventoryException {
            return InsufficientItemsInInventoryException(
                "There are not enough $itemType in inventory. Requested $requestedAmount but available $itemsInInventory"
            )
        }
    }

}
