package highway.cinema.showing.domain.inventory.instrastructure

import highway.cinema.showing.domain.inventory.InventoryRepository

class InventoryInfrastructureConfiguration {

    fun getDefault(): InventoryRepository {
        return DummyInventoryRepository()
    }
}
