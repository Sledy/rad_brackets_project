package highway.cinema.showing.domain.room.infrastructure

import highway.cinema.showing.domain.room.RoomAvailabilityRepository

class RoomAvailabilityInfraConfiguration {

    fun getDefault(): RoomAvailabilityRepository {
        return RoomAvailabilityRepositoryDummy()
    }
}
