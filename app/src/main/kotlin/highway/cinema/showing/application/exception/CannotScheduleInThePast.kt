package highway.cinema.showing.application.exception

import java.time.LocalDateTime

internal class CannotScheduleInThePast(scheduledTime: LocalDateTime, currentDateTime: LocalDateTime) : RuntimeException(
    "Cannot schedule showing in the past." +
            " Passed showing time $scheduledTime, current date $currentDateTime"
)
