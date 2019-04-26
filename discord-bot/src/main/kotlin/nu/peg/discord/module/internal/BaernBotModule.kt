package nu.peg.discord.module.internal

import discord4j.core.DiscordClient
import discord4j.core.event.domain.Event
import discord4j.core.event.domain.VoiceStateUpdateEvent
import discord4j.core.event.domain.message.MessageCreateEvent
import nu.peg.discord.command.CommandDispatcher
import nu.peg.discord.command.CommandParser
import nu.peg.discord.command.handler.internal.KeepOutEventHandler
import nu.peg.discord.config.DiscordProperties
import nu.peg.discord.event.EventListenerDispatcher
import nu.peg.discord.module.BaernModule
import nu.peg.discord.service.OnlineStatus
import nu.peg.discord.service.StatusService
import nu.peg.discord.util.getLogger
import javax.inject.Inject

class BaernBotModule
@Inject constructor(
        private val parser: CommandParser,
        private val cmdDispatcher: CommandDispatcher,
        private val statusService: StatusService,
        private val eventListenerDispatcher: EventListenerDispatcher,
        private val properties: DiscordProperties
) : BaernModule {
    companion object {
        private val LOGGER = getLogger(BaernBotModule::class)
    }

    override fun enable(client: DiscordClient): Boolean {
        client.eventDispatcher.on(MessageCreateEvent::class.java)
                .subscribe(::handleMessageCreateEvent)
        client.eventDispatcher.on(VoiceStateUpdateEvent::class.java)
                .subscribe(::handleVoiceStateUpdateEvent)
        client.eventDispatcher.on(Event::class.java)
                .subscribe(::handleAllEvents)

        statusService.setStatus(OnlineStatus.ONLINE, "BärnBot v${properties.bot.version} | ${properties.bot.prefix}?")

        LOGGER.info("Enabling BärnBot")
        return true
    }

    private fun handleMessageCreateEvent(event: MessageCreateEvent) {
        val message = event.message
        val command = parser.parse(message)
        if (command != null)
            cmdDispatcher.dispatch(command)
    }

    private fun handleVoiceStateUpdateEvent(event: VoiceStateUpdateEvent) {
        KeepOutEventHandler.handleEvent(event)
    }

    private fun handleAllEvents(event: Event) {
        eventListenerDispatcher.dispatch(event)
    }
}