package spectrum.sexplugin.hardcore.models

import org.junit.Test
import java.util.*

class StatisticsTest {

    @Test
    fun createEmpty() {
        val stats = Statistics("")

        assert(stats.lastDeathDate == null)
        assert(stats.respawnAt == null)
        assert(stats.isAlive(Date(0)))
    }

    @Test
    fun oneDeath() {
        val deaths = listOf(
            Death(10_000, ""),
        )
        val stats = Statistics("", 1_000, deaths)

        assert(stats.lastDeathDate == deaths.first().at)
        assert(stats.respawnAt?.time == 10_500L)
        assert(stats.isAlive(Date(11_000)))
        assert(!stats.isAlive(Date(10_400)))
    }

    @Test
    fun killEmpty() {
        val stats = Statistics("", 100)
        val new = stats.kill(Death(1_000))

        assert(new.lastDeathDate == Date(1_000))
        assert(new.respawnAt == Date(1_050))
        assert(new.isAlive(Date(1_200)))
        assert(!new.isAlive(Date(1_025)))
    }
}