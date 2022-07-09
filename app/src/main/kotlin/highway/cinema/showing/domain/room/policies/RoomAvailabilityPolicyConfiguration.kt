package highway.cinema.showing.domain.room.policies

import highway.cinema.showing.domain.room.RoomAvailabilityRepository

class RoomAvailabilityPolicyConfiguration {

    fun getDefault(roomAvailabilityRepository: RoomAvailabilityRepository): RoomAvailabilityPolicy {
        return DefaultRoomAvailability(roomAvailabilityRepository)
    }
}
