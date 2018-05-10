package nu.peg.discord.command.handler.internal

import nu.peg.discord.command.Command
import nu.peg.discord.command.handler.CommandHandler
import org.springframework.stereotype.Component

@Component
class MoveChannelCommandHandler : CommandHandler {
    override fun isAdminCommand() = true
    override fun getNames() = listOf("mc", "movechannel")
    override fun getDescription() = "Moves all users in the current channel to another one"

    override fun handle(command: Command) {
        val message = command.message
        val channel = message.channel

        val args = command.args
        if (args.isEmpty()) {
            channel.sendMessage("Usage: ${command.name} <channel name>")
            return
        }

        val messageGuild = message.guild
        val requesterChannel = message.author.voiceStates[messageGuild.longID].channel
        if (requesterChannel == null) {
            channel.sendMessage("You must be in a voice channel to perform this command")
            return
        }

        val channelName = args.joinToString(" ")
        val foundChannel = messageGuild.voiceChannels.firstOrNull { it.name.startsWith(channelName, true) }

        if (foundChannel == null) {
            channel.sendMessage("Found no channel for input <$channelName>")
            return
        }

        val users = requesterChannel.connectedUsers
        for (user in users) {
            user.moveToVoiceChannel(foundChannel)
        }
    }
}