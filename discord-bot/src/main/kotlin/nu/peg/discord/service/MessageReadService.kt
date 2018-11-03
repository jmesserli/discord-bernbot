package nu.peg.discord.service

import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IMessage
import java.time.LocalDateTime

interface MessageReadService {
    fun readMessages(channel: IChannel, before: LocalDateTime = LocalDateTime.now()): Iterator<IMessage>
}