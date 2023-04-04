package spectrum.sexplugin.hardcore

object DateUtil {
    private fun numbersUtil(number: Int, a: String, b: String, c: String): String {
        if (number in listOf(11, 12, 13, 14)) return "$number $c"

        return when (number % 10) {
            in listOf(1) -> "$number $a"
            in listOf(2, 3, 4) -> "$number $b"
            in listOf(5, 6, 7, 8, 9, 0) -> "$number $c"
            else -> ""
        }
    }

    private fun dateRusFormat(
        days: Int = 0,
        hours: Int = 0,
        minutes: Int = 0,
        seconds: Int = 0,
    ): String {
        val result = mutableListOf<String>()

        if (days != 0) result.add(numbersUtil(days, "день", "дня", "дней"))
        if (hours != 0) result.add(numbersUtil(hours, "час", "часа", "часов"))
        if (minutes != 0) result.add(numbersUtil(minutes, "минута", "минуты", "минут"))
        if (seconds != 0 || result.isEmpty())
            result.add(numbersUtil(seconds, "секунда", "секунды", "секунд"))

        return result.joinToString(" ")
    }

    private const val SEC = 1
    private const val MIN = SEC * 60
    private const val H = MIN * 60
    private const val D = H * 24

    //fun genStringTime(long: Int): String = genStringTime(long.toLong())
    fun genStringTime(long: Long): String {
        val secs = (long / 1000).toInt()

        val days = secs / D
        val hours = (secs-days*D) / H
        val minutes = (secs-days*D-hours*H) / MIN
        val seconds = secs-days*D-hours*H-minutes*MIN

        return dateRusFormat(
            days = days,
            hours = hours,
            minutes = minutes,
            seconds = seconds,
        )
    }
}