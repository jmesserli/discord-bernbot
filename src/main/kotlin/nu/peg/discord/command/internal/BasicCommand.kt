package nu.peg.discord.command.internal

import nu.peg.discord.command.Command
import sx.blah.discord.handle.obj.IMessage
import java.util.*

/**
 * An implementation of [Command] that just holds the data
 *
 * @author Joel Messerli @20.02.2017
 */
class BasicCommand(
        private val name: String,
        private val args: Array<String>,
        private val message: IMessage
) : Command {
    override fun getName() = name
    override fun getArgs() = args
    override fun getMessage() = message

    override fun toString(): String {
        return "BasicCommand(name='$name', args=${Arrays.toString(args)}, message=$message)"
    }
}