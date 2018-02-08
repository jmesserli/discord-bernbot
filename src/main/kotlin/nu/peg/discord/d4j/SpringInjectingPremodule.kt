package nu.peg.discord.d4j

import nu.peg.discord.config.StaticAppContext
import nu.peg.discord.module.BaernModule
import nu.peg.discord.module.internal.BaernBotModule
import nu.peg.discord.util.getLogger
import org.slf4j.Logger
import org.springframework.beans.factory.config.AutowireCapableBeanFactory
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.modules.IModule

@Suppress("unused")
class SpringInjectingPremodule : IModule, IListener<MessageReceivedEvent> {
    companion object {
        private val LOGGER: Logger = getLogger(SpringInjectingPremodule::class)
    }

    private var botModule: BaernModule? = null

    override fun enable(client: IDiscordClient): Boolean {
        botModule = StaticAppContext.context.autowireCapableBeanFactory
                .autowire(BaernBotModule::class.java, AutowireCapableBeanFactory.AUTOWIRE_CONSTRUCTOR, false) as? BaernModule

        return botModule?.enable(client) ?: false
    }

    override fun handle(event: MessageReceivedEvent?) {
        try {
            botModule?.handle(event)
        } catch (e: Exception) {
            LOGGER.error("Exception caught when handling message event", e)
        }
    }

    override fun getName() = "BärnBot Spring Injecting Module Loader"
    override fun getVersion() = "1.0.0"
    override fun getMinimumDiscord4JVersion() = "2.7.0"
    override fun getAuthor() = "Joel Messerli <hi.github@peg.nu>"
    override fun disable() {}
}