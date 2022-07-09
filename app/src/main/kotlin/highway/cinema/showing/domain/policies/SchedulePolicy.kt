package highway.cinema.showing.domain.policies

import highway.cinema.shared.kernel.exception.TimeRange
import java.time.LocalDateTime

interface SchedulePolicy {

    fun validateSchedulingDateTime(
        schedulingTimeLowerBoundary: LocalDateTime,
        schedulingTimeUpperBoundary: LocalDateTime
    ): Boolean

    fun getPolicyTimeRange(): TimeRange
}

