package nu.peg.discord.service

import kotlin.reflect.KClass

interface DatabaseService {
    fun writeDatabaseEvent(event: DatabaseEvent<*>)

    fun <T : Any> seekDatabaseEvents(eventType: KClass<T>): Iterator<DatabaseEvent<T>>
}