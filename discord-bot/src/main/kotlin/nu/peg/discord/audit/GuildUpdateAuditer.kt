package nu.peg.discord.audit

import nu.peg.discord.event.EventListener
import nu.peg.discord.service.AuditService
import org.springframework.stereotype.Component
import sx.blah.discord.handle.impl.events.guild.GuildUpdateEvent
import javax.inject.Inject

@Component
class GuildUpdateAuditer @Inject constructor(
        private val auditService: AuditService
) : EventListener<GuildUpdateEvent> {

    override fun onEvent(event: GuildUpdateEvent) {
        if (event.oldGuild.region.id === event.newGuild.region.id) return

        val oldRegion = event.oldGuild.region
        val newRegion = event.newGuild.region
        auditService.log("Server Region changed: ${oldRegion.name} (ID: ${oldRegion.id}) -> ${newRegion.name} (ID: ${newRegion.id})")
    }

    override fun getEventClass() = GuildUpdateEvent::class
}