package highway.cinema.showing.domain.room.infrastructure

import highway.cinema.showing.domain.room.domain.RoomAvailability
import highway.cinema.showing.domain.room.domain.RoomAvailabilityRepository
import java.time.DayOfWeek
import java.time.LocalTime
import java.util.Arrays
import java.util.stream.Collectors

internal class RoomAvailabilityRepositoryDummy : RoomAvailabilityRepository {

    override fun getRoomAvailabilityFor(roomNumber: Long): RoomAvailability {
        if (roomNumber == 6L) {
            return RoomAvailability(
                roomNumber,
                LocalTime.of(13, 0, 0),
                LocalTime.of(15, 0, 0),
                listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)
            )
        }

        val allDaysOfWeek: MutableList<DayOfWeek> = Arrays.stream(DayOfWeek.values()).collect(Collectors.toList())
        return RoomAvailability(
            roomNumber, LocalTime.of(10, 0, 0), LocalTime.of(22, 0, 0), allDaysOfWeek
        )
    }
}
