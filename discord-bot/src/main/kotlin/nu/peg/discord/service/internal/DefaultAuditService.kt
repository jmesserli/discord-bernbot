package nu.peg.discord.service.internal

import nu.peg.discord.audit.AuditEventEmbed
import nu.peg.discord.config.DiscordProperties
import nu.peg.discord.service.AuditService
import nu.peg.discord.util.DiscordClientListener
import nu.peg.discord.util.getLogger
import org.springframework.stereotype.Service
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.api.internal.json.objects.EmbedObject
import sx.blah.discord.handle.obj.IChannel
import java.awt.Color
import javax.inject.Inject

@Service
class DefaultAuditService @Inject constructor(
        private val properties: DiscordProperties
) : AuditService, DiscordClientListener {
    companion object {
        private val LOGGER = getLogger(DefaultAuditService::class)
    }

    private var discordClient: IDiscordClient? = null

    private val auditChannel: IChannel? by lazy { discordClient?.channels?.firstOrNull { it.name == properties.bot?.auditChannel } }
    override fun discordClientAvailable(client: IDiscordClient) {
        discordClient = client
    }

    override fun log(message: String) {
        log(AuditEventEmbed(Color.BLACK, "Unnamed Audit Event", message))
    }

    override fun log(eventEmbed: AuditEventEmbed) {
        if (discordClient == null) {
            LOGGER.info("Discord client not set, ignoring call to log")
            return
        }
        if (auditChannel == null) {
            LOGGER.info("Audit channel could not be found, ignoring call to log")
            return
        }

        auditChannel!!.sendMessage(EmbedObject(eventEmbed))
    }
}