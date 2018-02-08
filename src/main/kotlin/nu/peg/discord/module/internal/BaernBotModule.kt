package nu.peg.discord.module.internal

import nu.peg.discord.command.CommandDispatcher
import nu.peg.discord.command.CommandParser
import nu.peg.discord.module.BaernModule
import nu.peg.discord.util.getLogger
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import javax.inject.Inject

class BaernBotModule
@Inject constructor(
        private val parser: CommandParser,
        private val dispatcher: CommandDispatcher
) : BaernModule {
    companion object {
        private val LOGGER = getLogger(BaernBotModule::class)
    }

    private lateinit var client: IDiscordClient

    override fun enable(client: IDiscordClient): Boolean {
        this.client = client

        LOGGER.info("Enabling BärnBot")
        return true
    }

    override fun handle(event: MessageReceivedEvent?) {
        val message = event!!.message!!
        val command = parser.parse(message)
        if (command != null)
            dispatcher.dispatch(command)
    }
}