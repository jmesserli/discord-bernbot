package nu.peg.discord.audit

import nu.peg.discord.event.EventListener
import nu.peg.discord.service.AuditService
import org.springframework.stereotype.Component
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageDeleteEvent
import sx.blah.discord.handle.obj.IGuild
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.handle.obj.IUser
import java.awt.Color
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@Component
class MessageDeleteAuditer @Inject constructor(
        private val auditService: AuditService
) : EventListener<MessageDeleteEvent> {
    companion object {
        private val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")

        private fun getUserName(user: IUser, guild: IGuild): String {
            val displayName = user.getDisplayName(guild)
            val nameStringBuilder = StringBuilder("${user.name}#${user.discriminator}")

            if (user.name == displayName) {
                return nameStringBuilder.toString()
            }

            return nameStringBuilder.append(" aka $displayName").toString()
        }
    }

    override fun onEvent(event: MessageDeleteEvent) {
        val message: IMessage? = event.message

        if (message == null) {
            auditService.log(AuditEventEmbed(Color.RED, "❌ Deleted Message", "Deleted message with id ${event.messageID} (not cached)"))
            return
        }

        auditService.log(AuditEventEmbed(Color.RED, "Deleted Message", message.content, mapOf(
                "Author" to getUserName(message.author, message.guild),
                "Channel" to message.channel.name,
                "Creation Time" to message.timestamp.format(DATE_TIME_FORMATTER)
        )))
    }

    override fun getEventClass() = MessageDeleteEvent::class
}