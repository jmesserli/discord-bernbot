package nu.peg.discord.command.handler

import nu.peg.discord.command.Command

/**
 * TODO Short summary
 *
 * @author Joel Messerli @23.03.2017
 */
interface CommandHandler {
    fun isApplicable(command: Command): Boolean
    fun handle(command: Command)
}