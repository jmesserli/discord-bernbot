package nu.peg.discord.audit

import discord4j.core.`object`.Region
import discord4j.core.event.domain.guild.GuildUpdateEvent
import nu.peg.discord.event.EventListener
import nu.peg.discord.message.EmbedColors
import nu.peg.discord.service.AuditService
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import javax.inject.Inject

class GuildUpdateAuditer @Inject constructor(
        private val auditService: AuditService
) : EventListener<GuildUpdateEvent> {
    companion object {
        private fun formatRegion(region: Region?) =
                if (region != null) "${region.name} (ID: ${region.id})"
                else "Region not available"
    }

    override fun onEvent(event: GuildUpdateEvent) {
        Flux.concat(
                event.old.map { it.region }.orElse(Mono.just(null)),
                event.current.region
        ).buffer()
                .subscribe {
                    auditService.log(AuditEventEmbed(EmbedColors.LIGHT_BLUE, "🌍 Server Region Changed", "The server's voice region has changed", mapOf(
                            "Old Region" to formatRegion(it[0]),
                            "New Region" to formatRegion(it[1])
                    )))
                }
    }

    override fun getEventClass() = GuildUpdateEvent::class
}