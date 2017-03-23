package nu.peg.discord.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.handle.obj.Permissions
import kotlin.reflect.KClass

fun getLogger(clazz: KClass<*>): Logger =
        LoggerFactory.getLogger(clazz.java)

fun IMessage.authorIsAdmin(): Boolean {
    val permissions = author.getPermissionsForGuild(guild)
    return permissions.contains(Permissions.ADMINISTRATOR)
}