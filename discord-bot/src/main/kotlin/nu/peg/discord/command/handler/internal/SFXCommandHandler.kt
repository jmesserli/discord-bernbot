package nu.peg.discord.command.handler.internal

import nu.peg.discord.command.Command
import nu.peg.discord.message.BasicEmbed
import nu.peg.discord.message.EmbedColors
import nu.peg.discord.service.AudioService
import nu.peg.discord.service.MessageSendService
import org.springframework.stereotype.Component
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.handle.obj.IVoiceChannel
import javax.inject.Inject

@Component
class SFXCommandHandler @Inject constructor(
        private val audioService: AudioService,
        private val sendService: MessageSendService
) : AbstractVoiceChannelCommandHandler(false) {
    override fun isAdminCommand() = false
    override fun getNames() = listOf("sfx", "soundeffect")
    override fun getDescription() = "Plays a sound effect in your voice channel"

    override fun handle(command: Command, message: IMessage, userChannel: IVoiceChannel, targetChannel: IVoiceChannel?) {
        val guild = userChannel.guild

        if (command.args.isEmpty()) {
            sendUsage(command); return
        }

        audioService.joinVoice(userChannel)
        audioService.queueAudio(guild, SFXCommandHandler::class.java.getResource("/audio/414208__jacksonacademyashmore__airhorn.wav"))
        audioService.queueLeaveOnFinished(guild)
    }

    private fun sendUsage(command: Command) {
        sendService.send(command.message.channel, BasicEmbed(EmbedColors.ORANGE, "Usage: ${command.name} <sfx name | help>", "Command Usage").withHelpFooter())
    }
}