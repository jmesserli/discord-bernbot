package nu.peg.discord.command

import discord4j.core.`object`.entity.Message

/**
 * Parses an [IMessage] containing a command into a [Command]
 *
 * @author Joel Messerli @15.02.2017
 */
interface CommandParser {
    fun parse(message: Message): Command?
}