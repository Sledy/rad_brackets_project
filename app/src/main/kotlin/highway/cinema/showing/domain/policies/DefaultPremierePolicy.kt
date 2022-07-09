package highway.cinema.showing.domain.policies

import highway.cinema.shared.kernel.exception.TimeRange
import java.time.LocalTime

internal class DefaultPremierePolicy : PremierPolicy {
    override fun getPremiereRecommendedTimeRange(): TimeRange {
        return TimeRange(LocalTime.of(17, 0, 0), LocalTime.of(21, 0, 0))
    }
}
