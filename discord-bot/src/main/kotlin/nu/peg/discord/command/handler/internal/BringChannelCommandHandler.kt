package nu.peg.discord.command.handler.internal

import discord4j.core.`object`.entity.Message
import discord4j.core.`object`.entity.VoiceChannel
import nu.peg.discord.command.Command

class BringChannelCommandHandler : AbstractVoiceChannelCommandHandler(true) {
    override fun isAdminCommand() = true
    override fun getNames() = listOf("bc", "bringchannel")
    override fun getDescription() = "Brings users from another channel to the current one"

    override fun handle(command: Command, message: Message, userChannel: VoiceChannel, targetChannel: VoiceChannel?) {
        targetChannel!!.voiceStates
                .flatMap { it.user }
                .flatMap { it.asMember(userChannel.guildId) }
                .subscribe {
                    it.edit { spec ->
                        spec.setNewVoiceChannel(targetChannel.id)
                    }
                }
    }
}