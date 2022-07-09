package highway.cinema.showing.domain.policies

import highway.cinema.shared.kernel.exception.TimeRange

interface PremierPolicy {

    fun getPremiereRecommendedTimeRange(): TimeRange
}
