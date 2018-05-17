package nu.peg.discord.command.handler.internal

import nu.peg.discord.command.Command
import nu.peg.discord.command.handler.CommandHandler
import nu.peg.discord.message.BasicEmbed
import nu.peg.discord.message.EmbedColors
import nu.peg.discord.service.AudioService
import nu.peg.discord.service.MessageSendService
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class ForceLeaveAllAudioCommandHandler @Inject constructor(
        private val audioService: AudioService,
        private val sendService: MessageSendService
) : CommandHandler {
    override fun isAdminCommand() = true
    override fun getNames() = listOf("flaa", "forceleaveallaudio")
    override fun getDescription() = "Leaves all audio channels on all guilds"

    override fun handle(command: Command) {
        audioService.forceLeaveAll()
        sendService.send(command.message.channel, BasicEmbed(EmbedColors.ORANGE, "Sent command to leave all voice channels on all guilds", "Command result"))
    }
}