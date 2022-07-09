package highway.cinema.showing.domain.room

interface RoomAvailabilityRepository {

    fun getRoomAvailabilityFor(roomNumber: Long): RoomAvailability

}
