package highway.cinema.showing.domain

import highway.cinema.showing.application.ScheduledTime
import highway.cinema.showing.application.ShowingSchedulerService
import highway.cinema.showing.application.exception.CannotScheduleInThePast
import highway.cinema.showing.domain.movie.Movie
import highway.cinema.showing.domain.room.domain.Room
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import kotlin.test.assertFailsWith

class ShowingSchedulerServiceUnitTest {

    private val showingSchedulerService = ShowingSchedulerService()

    @Test
    fun `cannot schedule movie with the date in the past`() {
        // then
        assertFailsWith<CannotScheduleInThePast> {
            showingSchedulerService.schedule(
                havingSampleMovieWithoutPremiere(),
                havingSampleRoomWithAvailableSeatsEqualTo(),
                ScheduledTime(LocalDateTime.of(1999, 1, 1, 1, 1))
            )
        }

    }


    private fun havingSampleMovieWithoutPremiere() =
        Movie(title = "sample title", is3dMovie = true, hasPremiere = false, durationInMinutes = 120)

    private fun havingSampleRoomWithAvailableSeatsEqualTo() = Room(1, 15, 100)


}
