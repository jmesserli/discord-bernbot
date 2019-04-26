package nu.peg.discord.command.handler.internal

import discord4j.core.`object`.entity.Message
import discord4j.core.`object`.entity.VoiceChannel
import nu.peg.discord.command.Command
import nu.peg.discord.service.AudioService
import javax.inject.Inject

class AirhornCommandHandler @Inject constructor(
        private val audioService: AudioService
) : AbstractVoiceChannelCommandHandler(false) {
    override fun isAdminCommand() = false
    override fun getNames() = listOf("ah", "airhorn")
    override fun getDescription() = "Plays an airhorn sound in your channel"

    override fun handle(command: Command, message: Message, userChannel: VoiceChannel, targetChannel: VoiceChannel?) {
        val guild = userChannel.guild

        audioService.joinVoice(userChannel)
        audioService.queueAudio(guild, AirhornCommandHandler::class.java.getResource("/audio/414208__jacksonacademyashmore__airhorn.wav"))
        audioService.queueLeaveOnFinished(guild)
    }
}