package highway.cinema.showing.domain.room

class Room(val roomNumber: Long, val cleaningSlotInMinutes: Long, val roomSeats: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Room

        if (roomNumber != other.roomNumber) return false

        return true
    }

    override fun hashCode(): Int {
        return roomNumber.hashCode()
    }
}
