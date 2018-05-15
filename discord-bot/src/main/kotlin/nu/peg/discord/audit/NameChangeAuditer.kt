package nu.peg.discord.audit

import nu.peg.discord.event.EventListener
import nu.peg.discord.message.EmbedColors
import nu.peg.discord.service.AuditService
import org.springframework.stereotype.Component
import sx.blah.discord.handle.impl.events.guild.member.NicknameChangedEvent
import javax.inject.Inject

@Component
class NameChangeAuditer @Inject constructor(
        private val auditService: AuditService
) : EventListener<NicknameChangedEvent> {
    override fun onEvent(event: NicknameChangedEvent) {
        val user = event.user

        auditService.log(AuditEventEmbed(EmbedColors.LIGHT_BLUE, "🖍 Nickname change", "A user has changed his nickname", mapOf(
                "User" to "${user.name}#${user.discriminator}",
                "Old Nickname" to event.oldNickname.orElse(user.name),
                "New Nickname" to event.newNickname.orElse(user.name)
        )))
    }

    override fun getEventClass() = NicknameChangedEvent::class
}