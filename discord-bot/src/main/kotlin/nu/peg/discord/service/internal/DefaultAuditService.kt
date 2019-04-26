package nu.peg.discord.service.internal

import nu.peg.discord.config.DiscordProperties
import nu.peg.discord.service.AuditService
import nu.peg.discord.service.MessageSendService
import nu.peg.discord.util.getLogger
import org.springframework.stereotype.Service
import sx.blah.discord.handle.obj.IChannel
import java.awt.Color
import javax.inject.Inject

@Service
class DefaultAuditService @Inject constructor(
        private val properties: DiscordProperties,
        private val messageSendService: MessageSendService,
        private val channelService: DefaultChannelService
) : AuditService {
    companion object {
        private val LOGGER = getLogger(DefaultAuditService::class)
    }

    private val auditChannel: IChannel? by lazy {
        if (properties.bot == null || properties.bot!!.auditChannel == null) null
        else channelService.findByName(properties.bot!!.auditChannel!!)
    }

    override fun log(message: String) {
        log(AuditEventEmbed(Color.BLACK, "Unnamed Audit Event", message))
    }

    override fun log(eventEmbed: AuditEventEmbed) {
        if (auditChannel == null) {
            LOGGER.info("Audit channel could not be found, ignoring call to log")
            return
        }

        messageSendService.send(auditChannel!!, eventEmbed)
    }
}