package nu.peg.discord.message

import sx.blah.discord.handle.obj.IEmbed
import java.awt.Color
import java.time.LocalDateTime
import java.time.ZoneOffset

open class BasicEmbed(
        private val color: Color,
        private val content: String,
        private val title: String,
        private val fields: Map<String, String>?,
        private val footer: IEmbed.IEmbedFooter? = null,
        private val timestamp: LocalDateTime = LocalDateTime.now(ZoneOffset.UTC),
        private val author: IEmbed.IEmbedAuthor? = null,
        private val thumbnail: IEmbed.IEmbedImage? = null,
        private val video: IEmbed.IEmbedVideo? = null,
        private val embedProvider: IEmbed.IEmbedProvider? = null,
        private val image: IEmbed.IEmbedImage? = null,
        private val url: String? = null
) : IEmbed {
    override fun getType() = "rich"
    override fun getUrl() = url
    override fun getImage() = image
    override fun getEmbedProvider() = embedProvider
    override fun getVideo() = video
    override fun getThumbnail() = thumbnail
    override fun getAuthor() = author
    override fun getTimestamp() = timestamp
    override fun getFooter() = footer

    override fun getColor() = color
    override fun getDescription() = content
    override fun getTitle() = title
    override fun getEmbedFields(): MutableList<IEmbed.IEmbedField>? {
        if (fields == null || fields.isEmpty()) return null

        return fields.map {
            object : IEmbed.IEmbedField {
                override fun isInline() = false
                override fun getName() = it.key
                override fun getValue() = it.value
            }
        }.toMutableList()
    }
}