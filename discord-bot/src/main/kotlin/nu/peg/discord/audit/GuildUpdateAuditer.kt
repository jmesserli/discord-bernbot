package nu.peg.discord.audit

import nu.peg.discord.event.EventListener
import nu.peg.discord.service.AuditService
import nu.peg.discord.util.fromHex
import org.springframework.stereotype.Component
import sx.blah.discord.handle.impl.events.guild.GuildUpdateEvent
import sx.blah.discord.handle.obj.IRegion
import javax.inject.Inject

@Component
class GuildUpdateAuditer @Inject constructor(
        private val auditService: AuditService
) : EventListener<GuildUpdateEvent> {
    companion object {
        private fun formatRegion(region: IRegion) = "${region.name} (ID: ${region.id})"
    }

    override fun onEvent(event: GuildUpdateEvent) {
        if (event.oldGuild.region.id === event.newGuild.region.id) return

        val oldRegion = event.oldGuild.region
        val newRegion = event.newGuild.region

        auditService.log(AuditEventEmbed(fromHex("#00b2ff"), "Server Region Changed", "The server's voice region has changed", mapOf(
                "Old Region" to formatRegion(oldRegion),
                "New Region" to formatRegion(newRegion)
        )))
    }

    override fun getEventClass() = GuildUpdateEvent::class
}