package nu.peg.discord.config

import nu.peg.discord.util.DiscordClientListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("!no-bot")
class DiscordClientListenerInjector @Autowired constructor(
        private val listeners: List<DiscordClientListener>
) {
    fun injectListeners() {
        listeners.forEach { it.discordClientAvailable(StaticDiscordClient.client) }
    }
}