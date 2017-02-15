package nu.peg.discord.d4j

import nu.peg.discord.util.getLogger
import org.slf4j.Logger
import org.springframework.beans.factory.DisposableBean
import sx.blah.discord.api.IDiscordClient

class DisposableDiscordClient(val client: IDiscordClient) : DisposableBean, IDiscordClient by client {
    companion object {
        private val LOGGER: Logger = getLogger(DisposableDiscordClient::class)
    }

    override fun destroy() {
        LOGGER.info("Logging out client")
        client.logout()
    }
}