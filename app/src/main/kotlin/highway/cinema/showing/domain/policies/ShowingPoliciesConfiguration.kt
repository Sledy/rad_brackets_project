package highway.cinema.showing.domain.policies

class ShowingPoliciesConfiguration {

    fun getDefaultPremierPolicy(): PremierPolicy {
        return DefaultPremierePolicy()
    }

    fun getDefaultSchedulePolicy(): SchedulePolicy {
        return EveryDayWeekSchedulePolicy()
    }

}
