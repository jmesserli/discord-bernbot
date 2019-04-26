package nu.peg.discord.event

import discord4j.core.event.domain.Event
import javax.inject.Inject
import kotlin.reflect.full.isSubclassOf

class EventListenerDispatcher @Inject constructor(
        private val eventListeners: List<EventListener<*>>
) {
    fun <T : Event> dispatch(event: T) {
        eventListeners
                .filter { event::class.isSubclassOf(it.getEventClass()) }
                .forEach { @Suppress("UNCHECKED_CAST") (it as EventListener<T>).onEvent(event) }
    }
}