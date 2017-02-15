package nu.peg.discord.d4j

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.DisposableBean
import sx.blah.discord.api.IDiscordClient

class DisposableDiscordClient(val client: IDiscordClient) : DisposableBean, IDiscordClient by client {
    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(DisposableDiscordClient::class.java)
    }

    override fun destroy() {
        LOGGER.info("Logging out client")
        client.logout()
    }
}