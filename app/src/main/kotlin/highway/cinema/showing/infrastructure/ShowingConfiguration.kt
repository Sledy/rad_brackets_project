package highway.cinema.showing.infrastructure

import highway.cinema.showing.domain.ShowingRepository

class ShowingConfiguration {

    fun getDefault(): ShowingRepository {
        return ShowingRepositoryDummy()
    }
}
