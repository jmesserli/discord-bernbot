package nu.peg.discord.d4j

import nu.peg.discord.command.CommandDispatcher
import nu.peg.discord.util.getLogger
import org.springframework.stereotype.Component
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.MessageReceivedEvent
import sx.blah.discord.modules.IModule
import javax.inject.Inject

@Component
class BaernBotModule : IModule, IListener<MessageReceivedEvent> {
    companion object {
        private val LOGGER = getLogger(BaernBotModule::class)
    }

    @Inject
    lateinit var dispatcher: CommandDispatcher

    override fun handle(event: MessageReceivedEvent?) {
        val message = event!!.message!!
        dispatcher.dispatch(message)
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