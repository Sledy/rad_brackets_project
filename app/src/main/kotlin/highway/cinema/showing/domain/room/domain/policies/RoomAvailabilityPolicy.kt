package highway.cinema.showing.domain.room.domain.policies

import highway.cinema.showing.domain.room.domain.Room
import java.time.LocalDateTime

interface RoomAvailabilityPolicy {

    fun isRoomAvailableFor(
        occupationStartDateTime: LocalDateTime,
        occupationEndDateTime: LocalDateTime,
        room: Room
    ): Boolean
}
