package nu.peg.discord.service

import sx.blah.discord.handle.obj.IGuild
import sx.blah.discord.handle.obj.IVoiceChannel
import java.net.URL

interface AudioService {
    fun joinVoice(channel: IVoiceChannel)
    fun queueAudio(guild: IGuild, audio: URL)
    fun queueLeaveOnFinished(guild: IGuild)
}