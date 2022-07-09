package highway.cinema.showing.domain.exception

import highway.cinema.shared.kernel.exception.TimeRange

internal class SchedulingPolicyViolatedException private constructor(message: String?) : RuntimeException(message) {

    companion object {
        fun with(allowedTimeRange: TimeRange): SchedulingPolicyViolatedException {
            return SchedulingPolicyViolatedException(
                "Schedule policy has been violated." +
                        " Showing can be scheduled between ${allowedTimeRange.start} and ${allowedTimeRange.end}"
            )
        }
    }
}
