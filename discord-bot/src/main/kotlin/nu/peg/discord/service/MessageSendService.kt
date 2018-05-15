package nu.peg.discord.service

import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IEmbed

interface MessageSendService {
    fun send(channel: IChannel, message: String)
    fun send(channel: IChannel, embed: IEmbed, message: String? = null)
}