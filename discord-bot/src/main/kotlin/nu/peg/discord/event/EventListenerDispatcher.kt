package nu.peg.discord.event

import org.springframework.stereotype.Component
import sx.blah.discord.api.events.Event
import javax.inject.Inject
import kotlin.reflect.full.isSubclassOf

@Component
class EventListenerDispatcher @Inject constructor(
        private val eventListeners: List<EventListener<*>>
) {
    fun <T : Event> dispatch(event: T) {
        eventListeners
                .filter { event::class.isSubclassOf(it.getEventClass()) }
                .forEach { @Suppress("UNCHECKED_CAST") (it as EventListener<T>).onEvent(event) }
    }
}