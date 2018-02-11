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
        override val name: String,
        override val args: Array<String>,
        override val message: IMessage
) : Command {
    override fun toString(): String {
        return "BasicCommand(name='$name', args=${Arrays.toString(args)}, message=$message)"
    }
}