package highway.cinema.showing.domain.room.domain.policies

import highway.cinema.showing.domain.room.domain.RoomAvailabilityRepository

class RoomAvailabilityPolicyConfiguration {

    fun getDefault(roomAvailabilityRepository: RoomAvailabilityRepository): RoomAvailabilityPolicy {
        return DefaultRoomAvailability(roomAvailabilityRepository)
    }
}
