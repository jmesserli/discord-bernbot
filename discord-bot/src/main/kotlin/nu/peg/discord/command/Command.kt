package nu.peg.discord.command

import sx.blah.discord.handle.obj.IMessage

/**
 * A command is a parsed form of a raw string command
 *
 * @author Joel Messerli @15.02.2017
 */
interface Command {
    val name: String
    val args: Array<String>
    val message: IMessage
}