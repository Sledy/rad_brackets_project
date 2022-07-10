package highway.cinema.showing.application

import highway.cinema.showing.domain.ShowingFactory
import highway.cinema.showing.domain.movie.Movie
import highway.cinema.showing.domain.room.domain.Room

class ShowingSchedulerService {

    private val showingFactory: ShowingFactory = ShowingFactory.initWithDefaults()

    fun schedule(movie: Movie, room: Room, showTime: ScheduledTime) {
        showingFactory.createShowingFor(movie, room, showTime.showingDateTime)
    }

}
