package nu.peg.discord.command.handler.internal

import discord4j.core.spec.EmbedCreateSpec
import nu.peg.discord.command.Command
import nu.peg.discord.command.handler.CommandHandler
import nu.peg.discord.message.EmbedColors
import nu.peg.discord.service.AudioService
import nu.peg.discord.service.MessageSendService
import nu.peg.discord.util.init
import javax.inject.Inject

class ForceLeaveAllAudioCommandHandler @Inject constructor(
        private val audioService: AudioService,
        private val sendService: MessageSendService
) : CommandHandler {
    override fun isAdminCommand() = true
    override fun getNames() = listOf("flaa", "forceleaveallaudio")
    override fun getDescription() = "Leaves all audio channels on all guilds"

    override fun handle(command: Command) {
        audioService.forceLeaveAll()

        val embedSpec = EmbedCreateSpec().init {
            setColor(EmbedColors.ORANGE)
            setDescription("Sent command to leave all voice channels on all guilds")
            setTitle("Command result")
        }

        command.message.channel.subscribe {
            sendService.send(it, embedSpec)
        }
    }
}