package highway.cinema.showing.domain

import highway.cinema.showing.domain.room.Room
import java.time.LocalDateTime


interface ShowingRepository {


    fun isThereAnyShowingWithRoomForSpecifiedTime(
        showingStartTime: LocalDateTime,
        showingEndTime: LocalDateTime,
        showingRoom: Room
    ): Boolean
}
