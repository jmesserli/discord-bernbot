package nu.peg.discord.event

import sx.blah.discord.api.events.Event
import kotlin.reflect.KClass

interface EventListener<T : Event> {
    fun onEvent(event: T)
    fun getEventClass(): KClass<T>
}