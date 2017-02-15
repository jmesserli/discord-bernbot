package nu.peg.discord.command

/**
 * Parses a raw string command into a [Command]
 *
 * @author Joel Messerli @15.02.2017
 */
interface CommandParser {
    fun parse(command: String): Command
}