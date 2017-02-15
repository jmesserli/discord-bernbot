package nu.peg.discord.command

/**
 * A command is a parsed form of a raw string command
 *
 * @author Joel Messerli @15.02.2017
 */
interface Command {
    fun getName(): String
    fun getArgs(): Array<String>
}