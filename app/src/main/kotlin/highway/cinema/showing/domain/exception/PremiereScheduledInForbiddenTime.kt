package highway.cinema.showing.domain.exception

import highway.cinema.shared.kernel.exception.TimeRange

internal class PremiereScheduledInForbiddenTime private constructor(message: String?) : RuntimeException(message) {
    companion object {
        fun with(allowedTimeRange: TimeRange): PremiereScheduledInForbiddenTime {
            return PremiereScheduledInForbiddenTime(
                "Movie must be scheduled in following time range" + " <${allowedTimeRange.start} , ${allowedTimeRange.end}>"
            )
        }
    }
}
