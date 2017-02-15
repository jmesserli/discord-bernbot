package nu.peg.discord.command

import sx.blah.discord.handle.obj.IMessage

/**
 * A command dispatcher gets the raw messages from discord and dispatches it to the matching command processor
 *
 * @author Joel Messerli @15.02.2017
 */
interface CommandDispatcher {
    fun dispatch(inputMessage: IMessage)
}