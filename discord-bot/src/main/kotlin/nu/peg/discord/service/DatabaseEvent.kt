package nu.peg.discord.service

import kotlin.reflect.KClass

data class DatabaseEvent<T : Any>(
        val event: T,
        val eventType: String
) {
    constructor(event: T, eventTypeClass: KClass<out T>) : this(event, eventTypeClass.qualifiedName!!)
}