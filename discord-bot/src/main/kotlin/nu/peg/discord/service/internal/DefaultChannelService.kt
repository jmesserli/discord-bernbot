package nu.peg.discord.service.internal

import nu.peg.discord.service.ChannelService
import nu.peg.discord.util.DiscordClientListener
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.handle.obj.IChannel

@Service
class DefaultChannelService : ChannelService, DiscordClientListener {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(DefaultChannelService::class.java)
    }

    private var client: IDiscordClient? = null

    override fun discordClientAvailable(client: IDiscordClient) {
        this.client = client
    }

    override fun findByName(name: String): IChannel? {
        if (client == null) {
            LOGGER.info("Client not set, not searching channel")
            return null
        }

        return client!!.channels.firstOrNull { it.name == name }
    }
}