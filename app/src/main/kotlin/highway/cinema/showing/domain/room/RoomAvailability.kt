package highway.cinema.showing.domain.room

import java.time.DayOfWeek
import java.time.LocalTime

data class RoomAvailability(
    val roomNumber: Number,
    val openingTime: LocalTime,
    val closingTime: LocalTime,
    val daysWhenOpen: List<DayOfWeek>
)

