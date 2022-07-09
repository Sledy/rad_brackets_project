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
import java.util.*

class Showing {

    private var room: Room? = null
    private var movie: Movie? = null
    private var showingStartTime: LocalDateTime? = null
    private var showingEndTime: LocalDateTime? = null

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


    private var uuid: String = UUID.randomUUID().toString()

    @Version
    private var version: Long? = null


    private constructor(
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

    constructor() {}


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
            return showing
        }
    }


    private fun scheduleShowing(movie: Movie, showingStartTime: LocalDateTime, showingRoom: Room) {
        if (movie.hasPremiere) {
            validatePremiereRequirements(movie, showingStartTime)
        } else {
            val schedulePolicyIsNotViolated: Boolean = schedulePolicy?.validateSchedulingDateTime(
                showingStartTime, getEndShowingTimeFrom(showingStartTime, movie)
            ) ?: throw UnexpectedException("Schedule Policy has been not initialized")
            if (!schedulePolicyIsNotViolated) {
                throw SchedulingPolicyViolatedException.with(schedulePolicy?.getPolicyTimeRange()!!)
            }
        }

        validateRoomAvailability(movie, showingStartTime, showingRoom)

        if (movie.is3dMovie) {
            inventory?.reserveItems(ItemType.GLASSES, showingRoom.roomSeats)
                ?: throw UnexpectedException("Inventory has not been initialized inside Showing class")
        }

        assignShowingParameters(showingRoom, movie, showingStartTime)
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
        if (this.checkIfRoomIsAvailableForMovieInSpecifiedTime(movie, showingStartTime, showingRoom)) {
            throw RoomUnavailableException.with(movie, showingRoom)
        }
    }


    private fun checkIfRoomIsAvailableForMovieInSpecifiedTime(
        movie: Movie, showingStartTime: LocalDateTime, showingRoom: Room
    ): Boolean {
        val isAnyShowingAtThisTimeAndRoom: Boolean = this.showingRepository?.isThereAnyShowingWithRoomForSpecifiedTime(
            showingStartTime, showingStartTime.minusMinutes(movie.durationInMinutes), showingRoom
        ) ?: throw UnexpectedException("Showing repository has not been initialized for Showing class")

        return !isAnyShowingAtThisTimeAndRoom
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

        val premiereRequirementsViolated: Boolean = isPremiereStartAfterRequiredHour && isPremiereEndsBeforeRequiredTime

        if (premiereRequirementsViolated) {
            throw PremiereScheduledInForbiddenTime.with(premiereTimeRange)
        }

    }

    private fun getEndShowingTimeFrom(
        showingStartTime: LocalDateTime, movie: Movie
    ) = showingStartTime.plusMinutes(movie.durationInMinutes)

}



