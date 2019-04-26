package nu.peg.discord.service

import discord4j.core.`object`.entity.Channel
import discord4j.core.spec.EmbedCreateSpec


interface MessageSendService {
    fun send(channel: Channel, message: String)
    fun send(channel: Channel, embed: EmbedCreateSpec, message: String? = null)
}