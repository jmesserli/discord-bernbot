package nu.peg.discord.command.handler.internal

import nu.peg.discord.command.Command
import org.springframework.stereotype.Component
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.handle.obj.IVoiceChannel

@Component
class SwapChannelCommandHandler : AbstractVoiceChannelCommandHandler(true) {
    override fun isAdminCommand() = true
    override fun getNames() = listOf("sc", "swapchannel")
    override fun getDescription() = "Swaps the users of the current and another channel"

    override fun handle(command: Command, message: IMessage, userChannel: IVoiceChannel, targetChannel: IVoiceChannel?) {
        val targetUsers = targetChannel!!.connectedUsers
        val sourceUsers = userChannel.connectedUsers

        for (user in targetUsers) {
            user.moveToVoiceChannel(userChannel)
        }

        for (user in sourceUsers) {
            user.moveToVoiceChannel(targetChannel)
        }
    }
}