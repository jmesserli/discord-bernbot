package nu.peg.discord.d4j

import nu.peg.discord.command.CommandDispatcher
import nu.peg.discord.command.CommandParser
import nu.peg.discord.util.getLogger
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.MessageReceivedEvent
import sx.blah.discord.modules.IModule
import javax.inject.Inject

class BaernBotModule
@Inject constructor(
        val parser: CommandParser,
        val dispatcher: CommandDispatcher
) : IModule, IListener<MessageReceivedEvent> {
    companion object {
        private val LOGGER = getLogger(BaernBotModule::class)
    }

    override fun handle(event: MessageReceivedEvent?) {
        val message = event!!.message!!
        val command = parser.parse(message)
        if (command != null)
            dispatcher.dispatch(command)
    }

    override fun enable(client: IDiscordClient?): Boolean {
        LOGGER.info("Enabling $name $version")
        return true
    }

    override fun getName(): String = "BärnBot D4J Extension"
    override fun getVersion(): String = "0.0.1-SNAPSHOT"
    override fun getMinimumDiscord4JVersion(): String = "2.7.0"
    override fun getAuthor(): String = "Joel Messerli <hi.github@peg.nu>"
    override fun disable() {}
}