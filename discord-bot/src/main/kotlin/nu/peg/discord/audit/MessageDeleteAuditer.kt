package nu.peg.discord.audit

import discord4j.core.`object`.entity.Guild
import discord4j.core.`object`.entity.TextChannel
import discord4j.core.`object`.entity.User
import discord4j.core.event.domain.message.MessageDeleteEvent
import nu.peg.discord.event.EventListener
import nu.peg.discord.service.AuditService
import nu.peg.discord.util.mapOfResolvedMonos
import reactor.core.publisher.Mono
import java.awt.Color
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class MessageDeleteAuditer @Inject constructor(
        private val auditService: AuditService
) : EventListener<MessageDeleteEvent> {
    companion object {
        private val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")

        private fun getUserName(user: Optional<User>, guildMono: Mono<Guild>): Mono<String> {
            if (!user.isPresent) return Mono.just("User unavailable")

            return guildMono.flatMap { guild ->
                user.get().asMember(guild.id)
            }.map { member ->
                val displayName = member.displayName
                val nameStringBuilder = StringBuilder("${member.username}#${member.discriminator}")

                if (member.username == displayName) {
                    return@map nameStringBuilder.toString()
                }

                return@map nameStringBuilder.append(" aka $displayName").toString()
            }
        }
    }

    override fun onEvent(event: MessageDeleteEvent) {
        if (!event.message.isPresent) {
            auditService.log(AuditEventEmbed(Color.RED, "❌ Deleted Message", "Deleted message with id ${event.messageId} (not cached)"))
            return
        }

        val message = event.message.get()
        mapOfResolvedMonos(
                "Author" to getUserName(message.author, message.guild),
                "Channel" to message.channel.map { it as? TextChannel }.map { it?.name ?: "Name unavailable" },
                "Creation Time" to Mono.just(message.timestamp.atZone(ZoneId.systemDefault()).format(DATE_TIME_FORMATTER))
        ).subscribe {
            auditService.log(AuditEventEmbed(Color.RED, "❌ Deleted Message", message.content.orElse(""), it))
        }
    }

    override fun getEventClass() = MessageDeleteEvent::class
}