package highway.cinema.showing.domain.inventory.instrastructure

import highway.cinema.showing.domain.inventory.domain.InventoryRepository

class InventoryInfrastructureConfiguration {

    fun getDefault(): InventoryRepository {
        return DummyInventoryRepository()
    }
}
