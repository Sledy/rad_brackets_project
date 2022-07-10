package highway.cinema.showing.domain.movie

import java.util.UUID

class Movie(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val is3dMovie: Boolean,
    val hasPremiere: Boolean,
    val durationInMinutes: Long
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Movie

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}




