package nu.peg.discord.util

import discord4j.core.`object`.entity.Message
import discord4j.core.`object`.util.Permission
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
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
        unprefixedHexCode.toCharArray().joinToString("") { "$it$it" }
    } else unprefixedHexCode

    return Color(sixCharHexCode.toInt(16))
}

fun Message.authorIsAdmin(): Mono<Boolean> = authorAsMember
        .flatMap { it.basePermissions }
        .map { it.contains(Permission.ADMINISTRATOR) }

fun List<String>.containsIgnoreCase(str: String) = this.any { it.equals(str, true) }

fun <A, B> mapOfResolvedMonos(vararg pairs: Pair<A, Mono<B>>): Mono<Map<A, B>> {
    return Flux.fromIterable(pairs.asIterable())
            .map { it.first to it.second.block()!! }
            .buffer()
            .map { mapOf(*it.toTypedArray()) }
            .toMono()
}

fun <T> T.init(func: T.() -> Unit): T {
    func(this)
    return this
}