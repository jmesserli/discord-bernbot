package nu.peg.discord.command.handler.internal

import nu.peg.discord.command.Command
import nu.peg.discord.command.handler.CommandHandler
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.handle.obj.IVoiceChannel

abstract class AbstractVoiceChannelCommandHandler(
        private val hasTargetChannel: Boolean
) : CommandHandler {
    override fun handle(command: Command) {
        val message = command.message
        val messageChannel = message.channel
        val author = message.author
        val userVoiceChannel: IVoiceChannel? = author.getVoiceStateForGuild(message.guild).channel

        if (userVoiceChannel == null) {
            messageChannel.sendMessage("You need to be in a voice channel to use this command!")
            return
        }

        var targetChannel: IVoiceChannel? = null

        if (hasTargetChannel && command.args.isEmpty()) {
            messageChannel.sendMessage("You need to provide a target channel! (${command.name} <target channel>)")
            return
        } else if (hasTargetChannel) {
            val targetChannelName = command.args.joinToString(" ")
            targetChannel = message.guild.voiceChannels
                    .firstOrNull { it.name.contains(targetChannelName, true) }

            if (targetChannel == null) {
                messageChannel.sendMessage("Could not find your target voice channel <$targetChannelName>")
                return
            }
        }

        handle(command, message, userVoiceChannel, targetChannel)
    }

    abstract fun handle(command: Command, message: IMessage, userChannel: IVoiceChannel, targetChannel: IVoiceChannel?)
}