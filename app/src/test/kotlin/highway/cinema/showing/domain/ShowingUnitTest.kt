package highway.cinema.showing.domain

import highway.cinema.showing.domain.data.loading.persistSampleShowing
import highway.cinema.showing.domain.exception.PremiereScheduledInForbiddenTime
import highway.cinema.showing.domain.exception.RoomUnavailableException
import highway.cinema.showing.domain.inventory.exception.InsufficientItemsInInventoryException
import highway.cinema.showing.domain.movie.Movie
import highway.cinema.showing.domain.room.domain.Room
import highway.cinema.showing.infrastructure.ShowingRepositoryDummy
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Month
import kotlin.test.assertFailsWith

internal class ShowingUnitTest {

    private val showingRepository: ShowingRepository = ShowingRepositoryDummy()
    private val showingFactory = ShowingFactory.initWithDefaults()


    @BeforeEach
    fun clearDatabase() {
        showingRepository.deleteAllShowings()
    }

    @Test
    fun `should schedule the showing for normal movie without premiere and all rooms are available`() {
        // given
        val movie = havingSampleMovieWithoutPremiere()

        // and
        val room = havingSampleRoomWithAvailableSeatsEqualTo(100)

        // and
        val showingTime = LocalDateTime.of(LocalDate.of(2022, Month.JULY, 7), LocalTime.NOON)

        // and
        val showing: Showing = showingHasBeenScheduledWith(movie, room, showingTime)

        // when
        val persistedShowing: Showing? = whenFetchingScheduledShowing(showing)

        // then
        assertThatCreatedShowingHasSameParametersAsPersistedOne(showing, persistedShowing)
    }

    private fun whenFetchingScheduledShowing(showing: Showing) =
        showingRepository.getById(showing.id)


    @Test
    fun `should not allow schedule showing when given room is already occupied in given time`() {
        // given
        persistSampleShowing()

        // and
        val movie: Movie = havingSampleMovieWithoutPremiere()

        // and
        val room: Room = havingSampleRoomWithAvailableSeatsEqualTo(100)

        // and
        val showingTime = LocalDateTime.of(LocalDate.of(2022, Month.JANUARY, 1), LocalTime.of(13, 0))

        // then
        assertFailsWith<RoomUnavailableException> { showingHasBeenScheduledWith(movie, room, showingTime) }
    }

    @Test
    fun `should not allow to schedule premiere movie violating premiere policy`() {
        // and
        val movie = havingSampleMovieWithPremiere()

        // and
        val room = havingSampleRoomWithAvailableSeatsEqualTo(100)

        // and
        val showingTime = LocalDateTime.of(LocalDate.of(2022, Month.JANUARY, 1), LocalTime.of(13, 0))

        //then
        assertFailsWith<PremiereScheduledInForbiddenTime> { showingHasBeenScheduledWith(movie, room, showingTime) }
    }


    @Test
    fun `should not allow to schedule showing were there are not enough 3D glasses in inventory`() {
        // and
        val movie = havingSampleMovieWithoutPremiere()

        // and
        val room = havingSampleRoomWithAvailableSeatsEqualTo(1000)

        // and
        val showingTime = LocalDateTime.of(LocalDate.of(2022, Month.JANUARY, 1), LocalTime.of(13, 0))

        //then
        assertFailsWith<InsufficientItemsInInventoryException> {
            showingHasBeenScheduledWith(movie, room, showingTime)
        }
    }

    private fun showingHasBeenScheduledWith(
        movie: Movie,
        room: Room,
        showingTime: LocalDateTime
    ) = showingFactory.createShowingFor(movie, room, showingTime)

    private fun assertThatCreatedShowingHasSameParametersAsPersistedOne(
        showing: Showing, persistedShowing: Showing?
    ) {
        Assertions.assertNotNull(persistedShowing)

        Assertions.assertEquals(showing.id, persistedShowing!!.id)
        Assertions.assertEquals(showing.showingStartTime, persistedShowing.showingStartTime)
        Assertions.assertEquals(showing.showingEndTime, persistedShowing.showingEndTime)
        Assertions.assertEquals(showing.room, persistedShowing.room)
        Assertions.assertEquals(showing.movie, persistedShowing.movie)
    }

    private fun havingSampleMovieWithPremiere() =
        Movie(title = "sample title", is3dMovie = true, hasPremiere = true, durationInMinutes = 120)

    private fun havingSampleMovieWithoutPremiere() =
        Movie(title = "sample title", is3dMovie = true, hasPremiere = false, durationInMinutes = 120)

    private fun havingSampleRoomWithAvailableSeatsEqualTo(numberOfSeats: Int) = Room(1, 15, numberOfSeats)


}
