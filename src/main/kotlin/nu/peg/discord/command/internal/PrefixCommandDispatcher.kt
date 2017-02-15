package nu.peg.discord.command.internal

import nu.peg.discord.command.CommandDispatcher
import nu.peg.discord.config.DiscordProperties
import nu.peg.discord.util.getLogger
import org.springframework.stereotype.Component
import sx.blah.discord.handle.obj.IMessage

/**
 * An implementation for [CommandDispatcher] that extracts commands beginning with a prefix
 *
 * @author Joel Messerli @15.02.2017
 */
@Component
class PrefixCommandDispatcher(discordProperties: DiscordProperties) : CommandDispatcher {
    var prefix: String = discordProperties.bot!!.prefix!!

    companion object {
        private val LOGGER = getLogger(PrefixCommandDispatcher::class)
    }

    override fun dispatch(inputMessage: IMessage) {
        if (!inputMessage.content.startsWith(prefix)) return

        LOGGER.debug("Found prefixed message: ${inputMessage.id}")
    }
}