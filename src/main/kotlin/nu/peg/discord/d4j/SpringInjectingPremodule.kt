package nu.peg.discord.d4j

import nu.peg.discord.config.StaticAppContext
import nu.peg.discord.module.BaernModule
import org.springframework.beans.factory.config.AutowireCapableBeanFactory
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.MessageReceivedEvent
import sx.blah.discord.modules.IModule

/**
 * TODO Short summary
 *
 * @author Joel Messerli @23.03.2017
 */
class SpringInjectingPremodule : IModule, IListener<MessageReceivedEvent> {
    private var botModule: BaernModule? = null

    override fun enable(client: IDiscordClient): Boolean {
        botModule = StaticAppContext.context.autowireCapableBeanFactory
                .autowire(BaernBotModule::class.java, AutowireCapableBeanFactory.AUTOWIRE_CONSTRUCTOR, false) as? BaernModule

        return botModule?.enable(client) ?: false
    }

    override fun handle(event: MessageReceivedEvent?) {
        botModule?.handle(event)
    }

    override fun getName() = "BärnBot Spring Injecting Module Loader"
    override fun getVersion() = "1.0.0"
    override fun getMinimumDiscord4JVersion() = "2.7.0"
    override fun getAuthor() = "Joel Messerli <hi.github@peg.nu>"
    override fun disable() {}
}