package highway.cinema.showing.domain.data.loading

import highway.cinema.showing.domain.Showing
import highway.cinema.showing.domain.ShowingFactory
import highway.cinema.showing.domain.movie.Movie
import highway.cinema.showing.domain.room.domain.Room
import java.time.LocalDateTime
import java.time.Month

fun persistSampleShowing(): Showing {
    val showingFactory = ShowingFactory.initWithDefaults()
    return showingFactory.createShowingFor(
        Movie(title = "Movie 1", durationInMinutes = 120, is3dMovie = true, hasPremiere = false),
        Room(2, 30, 200),
        LocalDateTime.of(2022, Month.JANUARY, 1, 12, 0, 0)
    )
}
