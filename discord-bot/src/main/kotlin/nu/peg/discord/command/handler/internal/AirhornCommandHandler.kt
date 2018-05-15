package nu.peg.discord.command.handler.internal

import nu.peg.discord.command.Command
import nu.peg.discord.service.AudioService
import org.springframework.stereotype.Component
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.handle.obj.IVoiceChannel
import javax.inject.Inject

@Component
class AirhornCommandHandler @Inject constructor(
        private val audioService: AudioService
) : AbstractVoiceChannelCommandHandler(false) {
    override fun isAdminCommand() = false
    override fun getNames() = listOf("ah", "airhorn")
    override fun getDescription() = "Plays an airhorn sound in your channel"

    override fun handle(command: Command, message: IMessage, userChannel: IVoiceChannel, targetChannel: IVoiceChannel?) {
        val guild = userChannel.guild

        audioService.joinVoice(userChannel)
        audioService.queueAudio(guild, AirhornCommandHandler::class.java.getResource("/audio/414208__jacksonacademyashmore__airhorn.wav"))
        audioService.queueLeaveOnFinished(guild)
    }
}