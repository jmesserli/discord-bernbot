package nu.peg.discord.module.internal

import nu.peg.discord.command.CommandDispatcher
import nu.peg.discord.command.CommandParser
import nu.peg.discord.command.handler.internal.KeepOutEventHandler
import nu.peg.discord.config.DiscordClientListenerInjector
import nu.peg.discord.config.StaticDiscordClient
import nu.peg.discord.event.EventListenerDispatcher
import nu.peg.discord.module.BaernModule
import nu.peg.discord.service.OnlineStatus
import nu.peg.discord.service.StatusService
import nu.peg.discord.util.getLogger
import org.springframework.beans.factory.annotation.Value
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.api.events.Event
import sx.blah.discord.api.events.EventSubscriber
import sx.blah.discord.handle.impl.events.ReadyEvent
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.handle.impl.events.guild.voice.user.UserVoiceChannelEvent
import javax.inject.Inject

class BaernBotModule
@Inject constructor(
        private val parser: CommandParser,
        private val dispatcher: CommandDispatcher,
        @Value("\${discord.bot.version:}")
        private val version: String,
        @Value("\${discord.bot.prefix:.}")
        private val commandPrefix: String,
        private val statusService: StatusService,
        private val eventListenerDispatcher: EventListenerDispatcher,
        private val clientListenerInjector: DiscordClientListenerInjector
) : BaernModule {
    companion object {
        private val LOGGER = getLogger(BaernBotModule::class)
    }

    private lateinit var client: IDiscordClient
    private var ready: Boolean = false

    override fun enable(client: IDiscordClient): Boolean {
        this.client = client
        StaticDiscordClient.client = client
        client.dispatcher.registerListener(this)

        LOGGER.info("Enabling BärnBot")
        return true
    }

    @EventSubscriber
    fun handleMessageReceivedEvent(event: MessageReceivedEvent?) {
        val message = event!!.message!!
        val command = parser.parse(message)
        if (command != null)
            dispatcher.dispatch(command)
    }

    @EventSubscriber
    fun handleReadyEvent(event: ReadyEvent) {
        clientListenerInjector.injectListeners()

        statusService.setStatus(OnlineStatus.ONLINE, "BärnBot v$version | $commandPrefix?")
        ready = true
    }

    @EventSubscriber
    fun handleUserVoiceChannelEvent(event: UserVoiceChannelEvent) {
        KeepOutEventHandler.handleEvent(event)
    }

    @EventSubscriber
    fun handleAllEvents(event: Event) {
        if (!ready) return

        eventListenerDispatcher.dispatch(event)
    }
}