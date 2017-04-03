package nu.peg.discord.command.handler

import nu.peg.discord.command.Command

/**
 * TODO Short summary
 *
 * @author Joel Messerli @23.03.2017
 */
interface CommandHandler {
    fun isAdminCommand(): Boolean
    fun getNames(): List<String>
    fun handle(command: Command)
}