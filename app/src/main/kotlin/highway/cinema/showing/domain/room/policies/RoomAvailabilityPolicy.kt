package highway.cinema.showing.domain.room.policies

import highway.cinema.showing.domain.room.Room
import java.time.LocalDateTime

interface RoomAvailabilityPolicy {

    fun isRoomAvailableFor(
        occupationStartDateTime: LocalDateTime,
        occupationEndDateTime: LocalDateTime,
        room: Room
    ): Boolean
}
