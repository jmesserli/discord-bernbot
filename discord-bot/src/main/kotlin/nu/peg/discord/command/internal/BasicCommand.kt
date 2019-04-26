package nu.peg.discord.command.internal

import discord4j.core.`object`.entity.Message
import nu.peg.discord.command.Command
import java.util.*

/**
 * An implementation of [Command] that just holds the data
 *
 * @author Joel Messerli @20.02.2017
 */
class BasicCommand(
        override val name: String,
        override val args: Array<String>,
        override val message: Message
) : Command {
    override fun toString(): String {
        return "BasicCommand(name='$name', args=${Arrays.toString(args)}, message=$message)"
    }
}