package highway.cinema.showing.domain

import highway.cinema.showing.domain.movie.Movie
import highway.cinema.showing.domain.room.Room
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Month

internal class ShowingUnitTest {

    @Test
    fun `should schedule the showing for normal movie without premiere and all rooms are available`() {
        // given
        val showingFactory = ShowingFactory.initWithDefaults()

        // and
        val movie = Movie(title = "sample title", is3dMovie = true, hasPremiere = false, durationInMinutes = 120)

        // and
        val room = Room(1, 15, 100)

        // and
        val showingTime = LocalDateTime.of(LocalDate.of(2022, Month.JULY, 7), LocalTime.NOON)

        // and
        val showing: Showing = showingFactory.createShowingFor(movie, room, showingTime)

        // then
        Assertions.assertEquals(showingTime, showing.showingStartTime)
        Assertions.assertEquals(movie, showing.movie)
        Assertions.assertEquals(room, showing.room)
    }

    @Test
    fun `should not allow schedule showing when given room is already occupied in given time`() {
        // given
        val showingFactory = ShowingFactory.initWithDefaults()

        // and
        val movie = Movie(title = "sample title", is3dMovie = true, hasPremiere = false, durationInMinutes = 120)

        // and
        val room = Room(1, 15, 100)

        // and
        val showingTime = LocalDateTime.of(LocalDate.of(2022, Month.JULY, 7), LocalTime.NOON)

        // and
        val showing: Showing = showingFactory.createShowingFor(movie, room, showingTime)

    }


}
