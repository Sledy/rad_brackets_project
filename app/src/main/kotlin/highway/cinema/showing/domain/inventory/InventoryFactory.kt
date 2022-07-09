package highway.cinema.showing.domain.inventory

import highway.cinema.showing.domain.inventory.instrastructure.InventoryInfrastructureConfiguration

class InventoryFactory {

    companion object {
        fun getInventory(): Inventory {
            return Inventory(InventoryInfrastructureConfiguration().getDefault())
        }
    }


}
