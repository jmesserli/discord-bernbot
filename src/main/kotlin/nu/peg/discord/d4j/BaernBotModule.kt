package nu.peg.discord.d4j

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.modules.IModule

class BaernBotModule : IModule {
    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(BaernBotModule::class.java)
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