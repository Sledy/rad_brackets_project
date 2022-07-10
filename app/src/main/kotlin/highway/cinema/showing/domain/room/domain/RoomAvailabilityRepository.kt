package highway.cinema.showing.domain.room.domain

interface RoomAvailabilityRepository {

    fun getRoomAvailabilityFor(roomNumber: Long): RoomAvailability

}
