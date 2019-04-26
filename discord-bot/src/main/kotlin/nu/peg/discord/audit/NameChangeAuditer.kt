package nu.peg.discord.audit

import discord4j.core.event.domain.guild.MemberUpdateEvent
import nu.peg.discord.event.EventListener
import nu.peg.discord.message.EmbedColors
import nu.peg.discord.service.AuditService
import javax.inject.Inject

class NameChangeAuditer @Inject constructor(
        private val auditService: AuditService
) : EventListener<MemberUpdateEvent> {
    override fun onEvent(event: MemberUpdateEvent) {
        val oldNick = event.old.flatMap { it.nickname }.orElse("<?>")
        val newNick = event.currentNickname.orElse("<?>")

        event.member.subscribe { member ->
            auditService.log(AuditEventEmbed(EmbedColors.LIGHT_BLUE, "🖍 Nickname change", "A user has changed his nickname", mapOf(
                    "User" to "${member.username}#${member.discriminator}",
                    "Old Nickname" to oldNick,
                    "New Nickname" to newNick
            )))
        }
    }

    override fun getEventClass() = MemberUpdateEvent::class
}