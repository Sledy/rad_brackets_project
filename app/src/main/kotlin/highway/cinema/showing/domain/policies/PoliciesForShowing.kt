package highway.cinema.showing.domain.policies

import highway.cinema.showing.domain.room.domain.policies.RoomAvailabilityPolicy

data class PoliciesForShowing(
    val roomAvailabilityPolicy: RoomAvailabilityPolicy,
    val premierePolicy: PremierPolicy,
    val schedulePolicy: SchedulePolicy
)

