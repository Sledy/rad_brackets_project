package highway.cinema.showing.infrastructure

import highway.cinema.showing.domain.ShowingRepository
import highway.cinema.showing.domain.room.Room
import java.time.LocalDateTime

internal class ShowingRepositoryDummy : ShowingRepository {

    override fun isThereAnyShowingWithRoomForSpecifiedTime(
        showingStartTime: LocalDateTime,
        showingEndTime: LocalDateTime,
        showingRoom: Room
    ): Boolean {
        if (showingRoom.roomNumber == 1L
            && showingEndTime == LocalDateTime.MAX
            && showingStartTime == LocalDateTime.MIN
        ) {
            return false
        }
        return true
    }
}
