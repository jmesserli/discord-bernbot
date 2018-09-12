package nu.peg.discord.command.internal

import nu.peg.discord.command.Command
import nu.peg.discord.command.CommandDispatcher
import nu.peg.discord.command.handler.CommandHandler
import nu.peg.discord.util.authorIsAdmin
import nu.peg.discord.util.containsIgnoreCase
import nu.peg.discord.util.getLogger
import org.springframework.stereotype.Component
import javax.inject.Inject

/**
 * An implementation for [CommandDispatcher] that extracts commands beginning with a prefix
 *
 * @author Joel Messerli @15.02.2017
 */
@Component
class DefaultCommandDispatcher @Inject constructor(private val handlers: List<CommandHandler>) : CommandDispatcher {
    companion object {
        private val LOGGER = getLogger(DefaultCommandDispatcher::class)
    }

    override fun dispatch(command: Command) {
        if (command.message.author.isBot) return;

        val userIsAdmin = command.message.authorIsAdmin()
        for (handler in handlers) {
            val applicable = (!handler.isAdminCommand() || userIsAdmin) && handler.getNames().containsIgnoreCase(command.name)
            LOGGER.debug("Trying $handler for command $command (isApplicable: $applicable)")

            if (applicable) {
                handler.handle(command)
                return
            }
        }
        LOGGER.info("Found no matching handler for command $command")
    }
}
