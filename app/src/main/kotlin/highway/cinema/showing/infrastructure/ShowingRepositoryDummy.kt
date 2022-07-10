package highway.cinema.showing.infrastructure

import highway.cinema.showing.domain.Showing
import highway.cinema.showing.domain.ShowingRepository
import highway.cinema.showing.domain.room.domain.Room
import java.time.LocalDateTime

internal class ShowingRepositoryDummy : ShowingRepository {

    companion object {
        val database: MutableMap<String, Showing> = hashMapOf()
    }

    override fun isThereAnyShowingWithRoomForSpecifiedTime(
        showingStartTime: LocalDateTime,
        showingEndTime: LocalDateTime,
        showingRoom: Room
    ): Boolean {
        return database.values.any { showingFromDatabase ->
            if (showingFromDatabase.showingStartTime == null || showingFromDatabase.showingEndTime == null) {
                return false
            }

            return areDatesOverLapping(
                showingStartTime,
                showingEndTime,
                showingFromDatabase.showingStartTime!!,
                showingFromDatabase.showingEndTime!!
            )
        }
    }

    override fun save(showing: Showing): Showing {
        database[showing.id] = showing
        return showing
    }

    override fun getById(showingId: String): Showing? {
        return database[showingId]
    }

    override fun deleteAllShowings() {
        database.clear()
    }

    private fun areDatesOverLapping(
        firstDateTimeStart: LocalDateTime,
        firstDateTimeEnd: LocalDateTime,
        secondDateTimeStart: LocalDateTime,
        secondDateTimeEnd: LocalDateTime
    ): Boolean {
        return (firstDateTimeStart.isBefore(secondDateTimeStart) && firstDateTimeEnd.isAfter(secondDateTimeStart))
                || (firstDateTimeStart.isBefore(secondDateTimeEnd) && firstDateTimeEnd.isAfter(secondDateTimeEnd))
                || (firstDateTimeStart.isBefore(secondDateTimeEnd) && firstDateTimeEnd.isAfter(secondDateTimeEnd))
                || (firstDateTimeStart.isAfter(secondDateTimeEnd) && firstDateTimeEnd.isBefore(secondDateTimeEnd))
                || (firstDateTimeStart == secondDateTimeStart && firstDateTimeEnd == secondDateTimeEnd)
    }
}
