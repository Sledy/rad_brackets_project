package highway.cinema.showing.application

import highway.cinema.showing.application.exception.CannotScheduleInThePast
import java.time.LocalDateTime

data class ScheduledTime(val showingDateTime: LocalDateTime) {

    init {
        checkIfDateIsNotInThePast(showingDateTime)
    }

    private fun checkIfDateIsNotInThePast(showingDateTime: LocalDateTime) {
        val currentTime = LocalDateTime.now()
        if (showingDateTime.isBefore(currentTime)) {
            throw CannotScheduleInThePast(showingDateTime, currentTime)
        }
    }
}
