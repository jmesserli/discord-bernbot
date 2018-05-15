package nu.peg.discord.command.handler.internal

import nu.peg.discord.command.Command
import org.springframework.stereotype.Component
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.handle.obj.IVoiceChannel

@Component
class MoveChannelCommandHandler : AbstractVoiceChannelCommandHandler(true) {
    override fun isAdminCommand() = true
    override fun getNames() = listOf("mc", "movechannel")
    override fun getDescription() = "Moves all users in the current channel to another one"

    override fun handle(command: Command, message: IMessage, userChannel: IVoiceChannel, targetChannel: IVoiceChannel?) {
        val users = userChannel.connectedUsers
        users.forEach { it.moveToVoiceChannel(targetChannel!!) }
    }
}