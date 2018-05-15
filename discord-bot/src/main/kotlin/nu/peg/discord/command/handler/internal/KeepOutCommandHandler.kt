package nu.peg.discord.command.handler.internal

import nu.peg.discord.command.Command
import org.springframework.stereotype.Component
import sx.blah.discord.handle.impl.events.guild.voice.user.UserVoiceChannelEvent
import sx.blah.discord.handle.impl.events.guild.voice.user.UserVoiceChannelJoinEvent
import sx.blah.discord.handle.impl.events.guild.voice.user.UserVoiceChannelMoveEvent
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.handle.obj.IUser
import sx.blah.discord.handle.obj.IVoiceChannel
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class KeepOutCommandHandler : AbstractVoiceChannelCommandHandler(false) {
    override fun isAdminCommand() = true

    override fun getNames() = listOf("ko", "keepout")
    override fun getDescription() = "Keep a user out of the current channel for a period"
    override fun handle(command: Command, message: IMessage, userChannel: IVoiceChannel, targetChannel: IVoiceChannel?) {
        val channel = message.channel

        val targetUserArg = command.args[0]
        val targetUser = command.message.guild.users.firstOrNull { it.name.contains(targetUserArg, ignoreCase = true) }
        if (targetUser == null) {
            channel.sendMessage("No user found that matches \"$targetUserArg\"")
            return
        }

        val durationArg = command.args[1]
        val duration = try {
            Duration.parse(durationArg)
        } catch (e: Exception) {
            null
        }
        if (duration == null) {
            channel.sendMessage("Could not parse duration \"$durationArg\". Must be formatted according to https://en.wikipedia.org/wiki/ISO_8601#Durations")
            return
        }

        val dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
        val endTime = LocalDateTime.now().plus(duration)
        if (endTime.isBefore(LocalDateTime.now())) {
            channel.sendMessage("End of duration must be in the future (ends at ${endTime.format(dateFormat)})")
            return
        }

        KeepOutEventHandler.addUserChannelBan(targetUser, ChannelDuration(userChannel, endTime))
        channel.sendMessage("Keeping user ${targetUser.getDisplayName(channel.guild)} out of ${userChannel.name} until ${endTime.format(dateFormat)}")
    }
}

data class ChannelDuration(
        val channel: IVoiceChannel,
        val endTime: LocalDateTime
)

object KeepOutEventHandler {
    private val userKeepOutMap: MutableMap<IUser, ChannelDuration> = mutableMapOf()

    fun addUserChannelBan(user: IUser, channelDuration: ChannelDuration) {
        userKeepOutMap[user] = channelDuration

        val userVoiceState = user.voiceStates[channelDuration.channel.guild.longID]
        if (userVoiceState != null && userVoiceState.channel != null) {
            user.moveToVoiceChannel(findMoveChannel(channelDuration.channel))
        }
    }

    private fun userHasBanFromChannel(user: IUser, channel: IChannel): Boolean {
        val channelDuration = userKeepOutMap[user] ?: return false

        if (channelDuration.channel != channel) {
            return false
        }

        if (channelDuration.endTime.isBefore(LocalDateTime.now())) {
            userKeepOutMap.remove(user)
            return false
        }

        return true
    }

    private fun findMoveChannel(notChannel: IVoiceChannel): IVoiceChannel {
        val eligibleChannels = notChannel.guild.voiceChannels.filter { it != notChannel }

        return eligibleChannels.first { it.usersHere.isEmpty() }
                ?: return eligibleChannels.sortedBy { it.usersHere.size }.first<IVoiceChannel>()
    }

    fun handleEvent(userVoiceChannelEvent: UserVoiceChannelEvent) {
        if (userVoiceChannelEvent !is UserVoiceChannelJoinEvent && userVoiceChannelEvent !is UserVoiceChannelMoveEvent) {
            return
        }

        val user = userVoiceChannelEvent.user
        val targetChannel = userVoiceChannelEvent.voiceChannel

        val isBanned = userHasBanFromChannel(user, targetChannel)
        if (!isBanned) return

        if (userVoiceChannelEvent is UserVoiceChannelMoveEvent) {
            user.moveToVoiceChannel(userVoiceChannelEvent.oldChannel)
        }
        user.moveToVoiceChannel(findMoveChannel(targetChannel))
    }

    fun clearBans() {
        userKeepOutMap.clear()
    }
}