package nu.peg.discord.command

/**
 * A command dispatcher gets the parsed commands from discord and dispatches it to the matching command processor
 *
 * @author Joel Messerli @15.02.2017
 */
interface CommandDispatcher {
    fun dispatch(command: Command)
}