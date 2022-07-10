package highway.cinema.showing.domain.exception

import highway.cinema.showing.domain.movie.Movie
import highway.cinema.showing.domain.room.domain.Room

internal class RoomUnavailableException private constructor(message: String?) : RuntimeException(message) {

    companion object {
        fun with(movie: Movie, room: Room): RoomUnavailableException {
            return RoomUnavailableException(
                "Cannot schedule showing for movie ${movie.title}" +
                        " in room with number ${room.roomNumber}. Room is already booked"
            )
        }
    }

}
