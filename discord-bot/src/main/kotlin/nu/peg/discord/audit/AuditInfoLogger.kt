package nu.peg.discord.audit

import nu.peg.discord.config.StaticDiscordClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import sx.blah.discord.api.events.Event
import sx.blah.discord.handle.impl.events.guild.GuildUpdateEvent
import sx.blah.discord.handle.obj.IChannel
import javax.inject.Inject

@Component
class AuditInfoLogger @Inject constructor(
        @Value("\${discord.bot.auditChannel:}")
        private val auditChannelName: String
) {
    private val auditChannel: IChannel? by lazy { StaticDiscordClient.client.channels.firstOrNull { it.name == auditChannelName } }

    fun logEvent(event: Event) {
        if (auditChannel == null) return

        if (event is GuildUpdateEvent && event.oldGuild.region.id !== event.newGuild.region.id) {
            val oldRegion = event.oldGuild.region
            val newRegion = event.newGuild.region
            auditChannel!!.sendMessage("Server Region changed: ${oldRegion.name} (ID: ${oldRegion.id}) -> ${newRegion.name} (ID: ${newRegion.id})")
        }
    }
}