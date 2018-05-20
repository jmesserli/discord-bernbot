package nu.peg.discord.urlshortening

import nu.peg.discord.config.ProfileRegistry
import nu.peg.discord.event.EventListener
import nu.peg.discord.message.BasicEmbed
import nu.peg.discord.message.EmbedColors
import nu.peg.discord.service.LinkShortenerService
import nu.peg.discord.service.MessageSendService
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.handle.obj.IEmbed
import java.net.URL
import javax.inject.Inject

@Component
@Profile("!" + ProfileRegistry.NO_LINK_SHORTENER)
class UrlShorteningMessageListener @Inject constructor(
        private val linkShortenerService: LinkShortenerService,
        private val sendService: MessageSendService
) : EventListener<MessageReceivedEvent> {
    companion object {
        private val URL_REGEX_STR = "^(?:(?:https?|ftp)://)(?:\\S+(?::\\S*)?@)?(?:(?!(?:10|127)(?:\\.\\d{1,3}){3})(?!(?:169\\.254|192\\.168)(?:\\.\\d{1,3}){2})(?!172\\.(?:1[6-9]|2\\d|3[0-1])(?:\\.\\d{1,3}){2})(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[1-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|(?:(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)(?:\\.(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)*(?:\\.(?:[a-z\\u00a1-\\uffff]{2,}))\\.?)(?::\\d{2,5})?(?:[/?#]\\S*)?$"
        private val URL_REGEX = Regex(URL_REGEX_STR, RegexOption.IGNORE_CASE)
    }

    override fun getEventClass() = MessageReceivedEvent::class

    override fun onEvent(event: MessageReceivedEvent) {
        val messageContent = event.message.content
        if (messageContent.length < 25 || URL_REGEX.matchEntire(messageContent) == null) return
        val url = URL(messageContent)

        val shortened = linkShortenerService.shorten(messageContent) ?: return

        val authorDisplayName = event.message.author.getDisplayName(event.message.guild)
        sendService.send(event.message.channel, BasicEmbed(EmbedColors.LIGHT_BLUE,
                "The link by $authorDisplayName to ${url.host} has been shortened automatically: $shortened", "Shortened Link",
                url = shortened,
                footer = object : IEmbed.IEmbedFooter {
                    override fun getText() = "Shortened link"
                    override fun getIconUrl() = "https://cdn.peg.nu/files/resources/icons/material_link.png"
                }
        ))
        event.message.delete()
    }
}