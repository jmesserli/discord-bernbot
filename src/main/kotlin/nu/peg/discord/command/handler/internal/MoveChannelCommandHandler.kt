package nu.peg.discord.command.handler.internal

import nu.peg.discord.command.Command
import nu.peg.discord.command.handler.CommandHandler
import nu.peg.discord.util.authorIsAdmin
import org.springframework.stereotype.Component

/**
 * TODO Short summary
 *
 * @author Joel Messerli @23.03.2017
 */
@Component
class MoveChannelCommandHandler : CommandHandler {
    override fun isApplicable(command: Command): Boolean {
        val name = command.getName()
        return command.getMessage().authorIsAdmin() && (name.equals("movechannel", true) || name.equals("mc", true))
    }

    override fun handle(command: Command) {
        val message = command.getMessage()
        val channel = message.channel

        val args = command.getArgs()
        if (args.size == 0) {
            channel.sendMessage("Usage: ${command.getName()} <channel name>")
            return
        }

        val channelName = args.joinToString(" ")
        val foundChannel = message.guild.voiceChannels.filter { it.name.startsWith(channelName, true) }.firstOrNull()

        if (foundChannel == null) {
            channel.sendMessage("Found no channel for input <$channelName>")
            return
        }

        val users = message.author.connectedVoiceChannels.first().connectedUsers
        for (user in users) {
            user.moveToVoiceChannel(foundChannel)
        }
    }
}