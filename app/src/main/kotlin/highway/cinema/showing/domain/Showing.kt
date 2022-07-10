package highway.cinema.showing.domain

import highway.cinema.shared.kernel.exception.TimeRange
import highway.cinema.shared.kernel.exception.UnexpectedException
import highway.cinema.shared.kernel.infrastructure.Version
import highway.cinema.showing.domain.exception.PremiereScheduledInForbiddenTime
import highway.cinema.showing.domain.exception.RoomUnavailableException
import highway.cinema.showing.domain.exception.SchedulingPolicyViolatedException
import highway.cinema.showing.domain.inventory.Inventory
import highway.cinema.showing.domain.inventory.ItemType
import highway.cinema.showing.domain.movie.Movie
import highway.cinema.showing.domain.policies.PoliciesForShowing
import highway.cinema.showing.domain.policies.PremierPolicy
import highway.cinema.showing.domain.policies.SchedulePolicy
import highway.cinema.showing.domain.room.Room
import highway.cinema.showing.domain.room.policies.RoomAvailabilityPolicy
import java.time.LocalDateTime
import java.util.UUID

class Showing {

    var room: Room? = null
    var movie: Movie? = null
    var showingStartTime: LocalDateTime? = null
    var showingEndTime: LocalDateTime? = null

    @Transient
    private var roomAvailabilityPolicy: RoomAvailabilityPolicy? = null

    @Transient
    private var showingRepository: ShowingRepository? = null

    @Transient
    private var inventory: Inventory? = null

    @Transient
    private var premierePolicy: PremierPolicy? = null

    @Transient
    private var schedulePolicy: SchedulePolicy? = null


    val id: String

    @Version
    private var version: Long? = null


    companion object {
        fun createShowingWith(
            policiesForShowing: PoliciesForShowing,
            roomAvailabilityPolicy: RoomAvailabilityPolicy,
            showingRepository: ShowingRepository,
            inventory: Inventory,
            movie: Movie,
            showingStartTime: LocalDateTime,
            showingRoom: Room
        ): Showing {
            val showing = Showing(
                roomAvailabilityPolicy,
                showingRepository,
                inventory,
                policiesForShowing.premierePolicy,
                policiesForShowing.schedulePolicy
            )
            showing.scheduleShowing(movie, showingStartTime, showingRoom)
            return showingRepository.save(showing)
        }
    }

    private constructor(
        roomAvailabilityPolicy: RoomAvailabilityPolicy,
        showingRepository: ShowingRepository,
        inventory: Inventory,
        premierePolicy: PremierPolicy,
        schedulePolicy: SchedulePolicy,
        id: String = UUID.randomUUID().toString()
    ) {
        this.id = id
        this.roomAvailabilityPolicy = roomAvailabilityPolicy
        this.showingRepository = showingRepository
        this.inventory = inventory
        this.premierePolicy = premierePolicy
        this.schedulePolicy = schedulePolicy
    }

    constructor(id: String = UUID.randomUUID().toString()) {
        this.id = id
    }

    fun initializeAggregateDependencies(
        roomAvailabilityPolicy: RoomAvailabilityPolicy,
        showingRepository: ShowingRepository,
        inventory: Inventory,
        premierePolicy: PremierPolicy,
        schedulePolicy: SchedulePolicy
    ) {
        this.roomAvailabilityPolicy = roomAvailabilityPolicy
        this.showingRepository = showingRepository
        this.inventory = inventory
        this.premierePolicy = premierePolicy
        this.schedulePolicy = schedulePolicy
    }


    private fun scheduleShowing(movie: Movie, showingStartTime: LocalDateTime, showingRoom: Room) {
        checkShowingTimeRequirements(movie, showingStartTime)

        validateRoomAvailability(movie, showingStartTime, showingRoom)

        check3dMovieRequirements(movie, showingRoom)

        assignShowingParameters(showingRoom, movie, showingStartTime)
    }

    private fun check3dMovieRequirements(
        movie: Movie,
        showingRoom: Room
    ) {
        if (movie.is3dMovie) {
            inventory?.reserveItems(ItemType.GLASSES, showingRoom.roomSeats)
                ?: throw UnexpectedException("Inventory has not been initialized inside Showing class")
        }
    }

    private fun checkShowingTimeRequirements(
        movie: Movie,
        showingStartTime: LocalDateTime
    ) {
        if (movie.hasPremiere) {
            validatePremiereRequirements(movie, showingStartTime)
            return
        }
        val schedulePolicyIsNotViolated: Boolean = schedulePolicy?.validateSchedulingDateTime(
            showingStartTime, getEndShowingTimeFrom(showingStartTime, movie)
        ) ?: throw UnexpectedException("Schedule Policy has been not initialized")

        if (!schedulePolicyIsNotViolated) {
            throw SchedulingPolicyViolatedException.with(schedulePolicy?.getPolicyTimeRange()!!)
        }

    }

    private fun assignShowingParameters(
        showingRoom: Room, movie: Movie, showingStartTime: LocalDateTime
    ) {
        this.room = showingRoom
        this.movie = movie
        this.showingStartTime = showingStartTime
        this.showingEndTime = getOccupationEndTimeFrom(showingStartTime, movie, showingRoom)
    }

    private fun validateRoomAvailability(movie: Movie, showingStartTime: LocalDateTime, showingRoom: Room) {
        if (!this.roomIsUnavailableForTheShowing(movie, showingStartTime, showingRoom)) {
            throw RoomUnavailableException.with(movie, showingRoom)
        }
    }


    private fun roomIsUnavailableForTheShowing(
        movie: Movie, showingStartTime: LocalDateTime, showingRoom: Room
    ): Boolean {

        val canRoomBeReservedInThisTime: () -> Boolean = {
            roomAvailabilityPolicy?.isRoomAvailableFor(
                showingStartTime,
                getEndShowingTimeFrom(showingStartTime, movie),
                showingRoom
            ) ?: false
        }


        val isAnyShowingAtThisTimeAndRoom: () -> Boolean = {
            this.showingRepository?.isThereAnyShowingWithRoomForSpecifiedTime(
                showingStartTime, getEndShowingTimeFrom(showingStartTime, movie), showingRoom
            ) ?: throw UnexpectedException("Showing repository has not been initialized for Showing class")
        }

        return canRoomBeReservedInThisTime.invoke() && !isAnyShowingAtThisTimeAndRoom.invoke()
    }

    private fun getOccupationEndTimeFrom(showingStartTime: LocalDateTime, movie: Movie, room: Room): LocalDateTime {
        return getEndShowingTimeFrom(showingStartTime, movie).plusMinutes(room.cleaningSlotInMinutes)
    }

    private fun validatePremiereRequirements(movie: Movie, showingStartTime: LocalDateTime) {
        val premiereTimeRange: TimeRange = premierePolicy?.getPremiereRecommendedTimeRange()
            ?: throw UnexpectedException("PremierePolicy has not been initialized inside Showing class")

        val isPremiereStartAfterRequiredHour: Boolean = showingStartTime.toLocalTime().isAfter(premiereTimeRange.start)

        val showingEndTime: LocalDateTime = getEndShowingTimeFrom(showingStartTime, movie)
        val isPremiereEndsBeforeRequiredTime: Boolean = showingEndTime.toLocalTime().isBefore(premiereTimeRange.end)

        val premiereRequirementsViolated: Boolean = isPremiereStartAfterRequiredHour || isPremiereEndsBeforeRequiredTime

        if (premiereRequirementsViolated) {
            throw PremiereScheduledInForbiddenTime.with(premiereTimeRange)
        }
    }

    private fun getEndShowingTimeFrom(
        showingStartTime: LocalDateTime,
        movie: Movie
    ) = showingStartTime.plusMinutes(movie.durationInMinutes)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Showing

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}



