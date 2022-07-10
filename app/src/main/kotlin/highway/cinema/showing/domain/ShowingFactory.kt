package highway.cinema.showing.domain

import highway.cinema.showing.domain.inventory.domain.Inventory
import highway.cinema.showing.domain.inventory.domain.InventoryFactory
import highway.cinema.showing.domain.movie.Movie
import highway.cinema.showing.domain.policies.PoliciesForShowing
import highway.cinema.showing.domain.policies.PremierPolicy
import highway.cinema.showing.domain.policies.SchedulePolicy
import highway.cinema.showing.domain.policies.ShowingPoliciesConfiguration
import highway.cinema.showing.domain.room.domain.Room
import highway.cinema.showing.domain.room.domain.RoomAvailabilityRepository
import highway.cinema.showing.domain.room.domain.policies.RoomAvailabilityPolicy
import highway.cinema.showing.domain.room.domain.policies.RoomAvailabilityPolicyConfiguration
import highway.cinema.showing.domain.room.infrastructure.RoomAvailabilityInfraConfiguration
import highway.cinema.showing.infrastructure.ShowingConfiguration
import java.time.LocalDateTime

class ShowingFactory(
    private val roomAvailabilityPolicy: RoomAvailabilityPolicy,
    private val showingRepository: ShowingRepository,
    private val inventory: Inventory,
    private val premierePolicy: PremierPolicy,
    private val schedulePolicy: SchedulePolicy
) {


    companion object {
        fun initWithDefaults(): ShowingFactory {
            val showingRepository: ShowingRepository = ShowingConfiguration().getDefault()
            val roomAvailabilityRepository: RoomAvailabilityRepository =
                RoomAvailabilityInfraConfiguration().getDefault()
            val roomAvailabilityPolicy: RoomAvailabilityPolicy =
                RoomAvailabilityPolicyConfiguration().getDefault(roomAvailabilityRepository)
            val inventory: Inventory = InventoryFactory.getInventory()
            val premierePolicy: PremierPolicy = ShowingPoliciesConfiguration().getDefaultPremierPolicy()
            val schedulePolicy: SchedulePolicy = ShowingPoliciesConfiguration().getDefaultSchedulePolicy()

            return ShowingFactory(roomAvailabilityPolicy, showingRepository, inventory, premierePolicy, schedulePolicy)
        }
    }

    fun createShowingFor(movie: Movie, room: Room, showTime: LocalDateTime): Showing {
        return Showing.createShowingWith(
            PoliciesForShowing(this.roomAvailabilityPolicy, this.premierePolicy, this.schedulePolicy),
            this.roomAvailabilityPolicy,
            this.showingRepository,
            this.inventory,
            movie,
            showTime,
            room
        )
    }

}
