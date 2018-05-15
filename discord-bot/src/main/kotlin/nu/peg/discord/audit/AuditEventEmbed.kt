package nu.peg.discord.audit

import nu.peg.discord.message.BasicEmbed
import sx.blah.discord.handle.obj.IEmbed
import java.awt.Color

class AuditEventEmbed(color: Color, title: String, content: String, fields: Map<String, String>? = null) : BasicEmbed(
        color, content, title, fields,
        footer = object : IEmbed.IEmbedFooter {
            override fun getIconUrl() = "https://cdn.peg.nu/files/resources/icons/material_search.png"
            override fun getText() = "Audit Event"
        }
)