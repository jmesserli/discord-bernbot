package nu.peg.discord.command.internal

import nu.peg.discord.command.Command
import nu.peg.discord.command.CommandDispatcher
import nu.peg.discord.config.DiscordProperties
import nu.peg.discord.util.getLogger
import org.springframework.stereotype.Component
import javax.inject.Inject

/**
 * An implementation for [CommandDispatcher] that extracts commands beginning with a prefix
 *
 * @author Joel Messerli @15.02.2017
 */
@Component
class DefaultCommandDispatcher @Inject constructor(discordProperties: DiscordProperties) : CommandDispatcher {
    companion object {
        private val LOGGER = getLogger(DefaultCommandDispatcher::class)
    }

    override fun dispatch(command: Command) {
        LOGGER.info("Dispatching command: $command")
    }
}
