package highway.cinema.showing.domain.room.policies

import highway.cinema.showing.domain.room.Room
import highway.cinema.showing.domain.room.RoomAvailability
import highway.cinema.showing.domain.room.RoomAvailabilityRepository
import java.time.LocalDateTime
import java.time.LocalTime

internal class DefaultRoomAvailability(private val roomAvailabilityRepository: RoomAvailabilityRepository) :
    RoomAvailabilityPolicy {


    override fun isRoomAvailableFor(
        occupationStartDateTime: LocalDateTime, occupationEndDateTime: LocalDateTime, room: Room
    ): Boolean {
        val availability: RoomAvailability = roomAvailabilityRepository.getRoomAvailabilityFor(room.roomNumber)
        val isInAllowedWeekDay: Boolean = availability.daysWhenOpen.containsAll(
            listOf(
                occupationStartDateTime.dayOfWeek, occupationEndDateTime.dayOfWeek
            )
        )
        areOccupationHoursWithinAllowedRange(occupationStartDateTime, occupationEndDateTime, availability)
        return isInAllowedWeekDay && areOccupationHoursWithinAllowedRange(
            occupationStartDateTime, occupationEndDateTime, availability
        )
    }

    private fun areOccupationHoursWithinAllowedRange(
        occupationStartDateTime: LocalDateTime, occupationEndDateTime: LocalDateTime, availability: RoomAvailability
    ): Boolean {

        val occupationStartTime: LocalTime = occupationStartDateTime.toLocalTime()
        val isStartTimeValid: Boolean =
            occupationStartTime == availability.openingTime || occupationStartTime.isAfter(availability.openingTime)

        val occupationEndTime: LocalTime = occupationEndDateTime.toLocalTime()
        val isEndTimeValid: Boolean =
            occupationEndTime == availability.closingTime || occupationEndTime.isBefore(availability.closingTime)

        return isStartTimeValid && isEndTimeValid
    }
}
