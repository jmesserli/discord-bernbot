package nu.peg.discord.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.handle.obj.Permissions
import java.awt.Color
import kotlin.reflect.KClass

fun getLogger(clazz: KClass<*>): Logger =
        LoggerFactory.getLogger(clazz.java)

fun fromHex(hexCode: String): Color {
    val unprefixedHexCode = hexCode.removePrefix("#")

    if (!(unprefixedHexCode.length == 3 || unprefixedHexCode.length == 6)) {
        throw IllegalArgumentException("Hex code must be either three or six characters long")
    }

    val sixCharHexCode = if (unprefixedHexCode.length == 3) {
        unprefixedHexCode.toCharArray().map { "$it$it" }.joinToString("")
    } else unprefixedHexCode

    return Color(sixCharHexCode.toInt(16))
}

fun IMessage.authorIsAdmin(): Boolean {
    val permissions = author.getPermissionsForGuild(guild)
    return permissions.contains(Permissions.ADMINISTRATOR)
}

fun List<String>.containsIgnoreCase(str: String) = this.any { it.equals(str, true) }
