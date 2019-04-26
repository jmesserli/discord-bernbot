package nu.peg.discord.command.handler.internal

import discord4j.core.`object`.entity.Channel
import discord4j.core.`object`.entity.Message
import discord4j.core.`object`.entity.VoiceChannel
import nu.peg.discord.command.Command
import nu.peg.discord.command.handler.CommandHandler
import nu.peg.discord.util.getLogger
import java.time.Duration
import java.util.*

abstract class AbstractVoiceChannelCommandHandler(
        private val hasTargetChannel: Boolean
) : CommandHandler {
    companion object {
        private val LOGGER = getLogger(AbstractVoiceChannelCommandHandler::class)
    }

    override fun handle(command: Command) {
        val message = command.message
        if (!message.author.isPresent) {
            LOGGER.info("Exiting because author could not be determined")
            return
        }

        val userVoiceChannel: Optional<VoiceChannel> = message.authorAsMember
                .flatMap { it.voiceState }
                .flatMap { it.channel }
                .blockOptional(Duration.ofSeconds(5))

        if (!userVoiceChannel.isPresent) {
            message.channel.subscribe {
                it.createMessage("You need to be in a voice channel to use this command!")
            }
            return
        }

        var targetChannel: VoiceChannel? = null

        if (hasTargetChannel && command.args.isEmpty()) {
            message.channel.subscribe { it.createMessage("You need to provide a target channel! (${command.name} <target channel>)") }
            return
        } else if (hasTargetChannel) {
            val targetChannelName = command.args.joinToString(" ")

            val targetOptional = message.guild
                    .flatMapMany { it.channels }
                    .filter { it.type == Channel.Type.GUILD_VOICE && it.name.contains(targetChannelName, true) }
                    .next()
                    .blockOptional(Duration.ofSeconds(5))

            if (!targetOptional.isPresent) {
                message.channel.subscribe { it.createMessage("Could not find your target voice channel <$targetChannelName>") }
                return
            }

            targetChannel = targetOptional.get() as VoiceChannel
        }

        handle(command, message, userVoiceChannel.get(), targetChannel)
    }

    abstract fun handle(command: Command, message: Message, userChannel: VoiceChannel, targetChannel: VoiceChannel?)
}