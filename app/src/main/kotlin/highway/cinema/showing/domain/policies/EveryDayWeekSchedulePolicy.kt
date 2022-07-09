package highway.cinema.showing.domain.policies

import highway.cinema.shared.kernel.exception.TimeRange
import java.time.LocalDateTime
import java.time.LocalTime

internal class EveryDayWeekSchedulePolicy : SchedulePolicy {

    private val lowerBoundary: LocalTime = LocalTime.of(8, 0)
    private val upperBoundary: LocalTime = LocalTime.of(22, 0)


    override fun validateSchedulingDateTime(
        schedulingTimeLowerBoundary: LocalDateTime, schedulingTimeUpperBoundary: LocalDateTime
    ): Boolean {
        val scheduledLowerBoundary = schedulingTimeLowerBoundary.toLocalTime()
        val isLowerBoundaryValid: Boolean =
            scheduledLowerBoundary == lowerBoundary || scheduledLowerBoundary.isAfter(lowerBoundary)

        val scheduledUpperBoundary = schedulingTimeUpperBoundary.toLocalTime()
        val isUpperBoundaryValid: Boolean =
            scheduledUpperBoundary == upperBoundary || scheduledUpperBoundary.isBefore(upperBoundary)

        return isLowerBoundaryValid && isUpperBoundaryValid
    }

    override fun getPolicyTimeRange(): TimeRange {
        return TimeRange(lowerBoundary, upperBoundary)
    }
}








