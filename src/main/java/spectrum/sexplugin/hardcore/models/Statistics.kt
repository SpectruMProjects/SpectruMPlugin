package spectrum.sexplugin.hardcore.models

import spectrum.sexplugin.hardcore.exeptions.UserCantBeKilledBeforeRespawnException
import java.util.Date

data class Death(
    val at: Date,
    val reason: String
)

/**
 * @property timeOnServer Время проведенное игроком на сервере, измеряется в миллисекнудах
 */
data class Statistics(
    val userId: String,
    val timeOnServer: Long = 0,
    val deaths: List<Death> = emptyList()
) {
    val lastDeathDate: Date? = deaths.maxByOrNull { it.at }?.at
    val respawnAt: Date? = lastDeathDate?.let { respawnAt(timeOnServer, it) }

    fun kill(reason: String) = kill(Death(Date(), reason))
    fun kill(death: Death): Statistics {
        if (Date() < (respawnAt ?: Date(0)))
            throw UserCantBeKilledBeforeRespawnException()

        return copy(
            deaths = deaths + death,
        )
    }

    /** Увеличивает время проведённое на сервере */
    operator fun plus(value: Long) = copy(timeOnServer = timeOnServer + value)
}

private const val WEEK: Long = 1000 * 60 * 60 * 24 * 7
fun respawnAt(
    timeOnServer: Long,
    lastDeathDate: Date
): Date = Date(lastDeathDate.time + (timeOnServer / 2).coerceAtMost(WEEK))