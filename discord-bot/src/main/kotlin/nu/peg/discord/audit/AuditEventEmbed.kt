package nu.peg.discord.audit

import sx.blah.discord.handle.obj.IEmbed
import java.awt.Color
import java.time.LocalDateTime
import java.time.ZoneOffset

data class AuditEventEmbed(
        private val color: Color,
        private val title: String,
        val content: String,
        val fields: Map<String, String>? = null
) : IEmbed {
    //region Default values

    override fun getType() = "rich"
    override fun getUrl() = null
    override fun getImage() = null
    override fun getEmbedProvider() = null
    override fun getVideo() = null
    override fun getThumbnail() = null
    override fun getAuthor() = null
    override fun getTimestamp() = LocalDateTime.now(ZoneOffset.UTC)!!
    override fun getFooter() = object : IEmbed.IEmbedFooter {
        override fun getIconUrl() = "https://cdn.peg.nu/files/resources/icons/material_search.png"
        override fun getText() = "Audit Event"
    }

    //endregion

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