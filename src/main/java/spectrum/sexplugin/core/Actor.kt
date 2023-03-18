package spectrum.sexplugin.core

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.SendChannel

abstract class Actor<E> {
    protected var actor: SendChannel<E>? = null
    @OptIn(ExperimentalCoroutinesApi::class)
    val isActive
        get() = actor?.isClosedForSend == false

    init {
        start()
    }

    /**
     * Стартует актор если он ещё не был запущен
     */
    fun start() {
        if (isActive) return
        actor = createActor()
    }

    /**
     * Останавливает актор
     */
    fun stop() {
        actor?.close()
        actor = null
    }

    /**
     * Перезапскает актор, если он был не запущен, то просто запускает
     */
    fun restart() {
        if (isActive) stop()
        start()
    }

    protected abstract fun createActor(): SendChannel<E>
}