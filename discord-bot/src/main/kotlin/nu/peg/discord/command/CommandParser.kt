package nu.peg.discord.command

import sx.blah.discord.handle.obj.IMessage

/**
 * Parses an [IMessage] containing a command into a [Command]
 *
 * @author Joel Messerli @15.02.2017
 */
interface CommandParser {
    fun parse(message: IMessage): Command?
}