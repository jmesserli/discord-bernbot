package nu.peg.discord.service.internal

import nu.peg.discord.service.MessageSendService
import nu.peg.discord.util.DiscordClientListener
import org.springframework.stereotype.Service
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.api.internal.json.objects.EmbedObject
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IEmbed

@Service
class DefaultMessageSendService : MessageSendService, DiscordClientListener {
    private lateinit var discordClient: IDiscordClient

    override fun discordClientAvailable(client: IDiscordClient) {
        discordClient = client
    }

    override fun send(channel: IChannel, message: String) {
        channel.sendMessage(message)
    }

    override fun send(channel: IChannel, embed: IEmbed, message: String?) {
        channel.sendMessage(message, EmbedObject(embed))
    }
}