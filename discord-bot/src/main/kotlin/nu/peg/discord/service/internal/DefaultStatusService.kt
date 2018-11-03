package nu.peg.discord.service.internal

import nu.peg.discord.service.OnlineStatus
import nu.peg.discord.service.StatusService
import nu.peg.discord.util.DiscordClientListener
import nu.peg.discord.util.getLogger
import org.slf4j.Logger
import org.springframework.stereotype.Component
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.handle.obj.ActivityType
import sx.blah.discord.handle.obj.StatusType

@Component
class DefaultStatusService : StatusService, DiscordClientListener {
    companion object {
        private val LOGGER: Logger = getLogger(DefaultStatusService::class)
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
            OnlineStatus.ONLINE -> nonNullClient.changePresence(StatusType.ONLINE, ActivityType.PLAYING, message)
            OnlineStatus.DND -> nonNullClient.changePresence(StatusType.DND, ActivityType.PLAYING, message)
            OnlineStatus.AFK -> nonNullClient.changePresence(StatusType.IDLE, ActivityType.PLAYING, message)
        }

        LOGGER.info("Set status to {}, with message {}", onlineStatus, message)
    }
}