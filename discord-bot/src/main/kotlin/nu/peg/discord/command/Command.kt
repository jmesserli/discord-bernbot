package nu.peg.discord.command

import discord4j.core.`object`.entity.Message

/**
 * A command is a parsed form of a raw string command
 *
 * @author Joel Messerli @15.02.2017
 */
interface Command {
    val name: String
    val args: Array<String>
    val message: Message
}