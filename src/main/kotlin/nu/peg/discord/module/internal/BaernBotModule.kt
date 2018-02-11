package nu.peg.discord.module.internal

import nu.peg.discord.command.CommandDispatcher
import nu.peg.discord.command.CommandParser
import nu.peg.discord.module.BaernModule
import nu.peg.discord.util.getLogger
import org.springframework.beans.factory.annotation.Value
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.api.events.EventSubscriber
import sx.blah.discord.handle.impl.events.ReadyEvent
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import javax.inject.Inject

class BaernBotModule
@Inject constructor(
        private val parser: CommandParser,
        private val dispatcher: CommandDispatcher,
        @Value("\${discord.bot.version:}")
        private val version: String
) : BaernModule {
    companion object {
        private val LOGGER = getLogger(BaernBotModule::class)
    }

    private lateinit var client: IDiscordClient

    override fun enable(client: IDiscordClient): Boolean {
        this.client = client
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
        client.online("BärnBot v$version")
    }
}