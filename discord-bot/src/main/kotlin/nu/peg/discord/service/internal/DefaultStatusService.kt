package nu.peg.discord.service.internal

import nu.peg.discord.service.OnlineStatus
import nu.peg.discord.service.StatusService
import nu.peg.discord.util.DiscordClientListener
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import sx.blah.discord.api.IDiscordClient

@Component
class DefaultStatusService : StatusService, DiscordClientListener {
    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(DefaultStatusService::class.java)
    }

    private var client: IDiscordClient? = null

    override fun discordClientAvailable(client: IDiscordClient) {
        this.client = client
    }

    override fun setStatus(onlineStatus: OnlineStatus, message: String?) {
        if (client == null) {
            LOGGER.info("Not setting status because client is null")
            return
        }

        val nonNullClient = client!!
        when (onlineStatus) {
            OnlineStatus.ONLINE -> nonNullClient.online(message)
            OnlineStatus.DND -> nonNullClient.dnd(message)
            OnlineStatus.AFK -> nonNullClient.idle(message)
        }

        LOGGER.info("Set status to {}, with message {}", onlineStatus, message)
    }
}