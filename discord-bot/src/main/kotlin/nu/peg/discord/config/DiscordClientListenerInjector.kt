package nu.peg.discord.config

import nu.peg.discord.util.DiscordClientListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import sx.blah.discord.api.IDiscordClient
import javax.annotation.PostConstruct

@Component
class DiscordClientListenerInjector @Autowired constructor(
        private val discordClient: IDiscordClient,
        private val listeners: List<DiscordClientListener>
) {

    @PostConstruct
    private fun injectListeners() {
        listeners.forEach { it.discordClientAvailable(discordClient) }
    }
}