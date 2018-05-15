package nu.peg.discord.command.handler.internal

import nu.peg.discord.command.Command
import org.springframework.stereotype.Component
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.handle.obj.IVoiceChannel

@Component
class BringChannelCommandHandler : AbstractVoiceChannelCommandHandler(true) {
    override fun isAdminCommand() = true
    override fun getNames() = listOf("bc", "bringchannel")
    override fun getDescription() = "Brings users from another channel to the current one"

    override fun handle(command: Command, message: IMessage, userChannel: IVoiceChannel, targetChannel: IVoiceChannel?) {
        val users = targetChannel!!.connectedUsers
        users.forEach { it.moveToVoiceChannel(userChannel) }
    }
}