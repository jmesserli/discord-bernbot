package nu.peg.discord.service.internal

import nu.peg.discord.service.MessageReadService
import nu.peg.discord.util.DiscordClientListener
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IMessage
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class DefaultMessageReadService : MessageReadService, DiscordClientListener {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(DefaultMessageReadService::class.java)
    }

    private var discordClient: IDiscordClient? = null

    override fun discordClientAvailable(client: IDiscordClient) {
        discordClient = client
    }

    override fun readMessages(channel: IChannel, before: LocalDateTime): Iterator<IMessage> {
        if (discordClient == null) {
            LOGGER.info("No discord client set, not reading messages")
            return emptyList<IMessage>().iterator()
        }

        return channel.getMessageHistoryFrom(before.atZone(ZoneId.systemDefault()).toInstant(), 100).iterator()
    }
}