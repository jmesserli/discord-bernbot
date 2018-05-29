package nu.peg.discord.service.internal

import nu.peg.discord.service.AudioService
import nu.peg.discord.util.DiscordClientListener
import nu.peg.discord.util.getLogger
import org.springframework.stereotype.Service
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.handle.obj.IGuild
import sx.blah.discord.handle.obj.IVoiceChannel
import sx.blah.discord.util.audio.AudioPlayer
import java.net.URL

@Service
class DefaultAudioService : AudioService, DiscordClientListener {
    companion object {
        private val LOGGER = getLogger(DefaultAudioService::class)
    }

    private lateinit var discordClient: IDiscordClient

    private val joinedGuilds: MutableSet<IGuild> = mutableSetOf()
    private val guildLeaveThreads: MutableMap<IGuild, Thread> = mutableMapOf()

    override fun discordClientAvailable(client: IDiscordClient) {
        discordClient = client
    }

    override fun joinVoice(channel: IVoiceChannel) {
        val guild = channel.guild

        if (!joinedGuilds.add(guild)) {
            LOGGER.info("Not joining voice channel ${channel.name} on ${guild.name} because the bot has already joined a channel on this guild")
            return
        }

        LOGGER.debug("Joined channel ${channel.name} on ${guild.name}")
        channel.join()
    }

    override fun queueAudio(guild: IGuild, audio: URL) {
        if (guild !in joinedGuilds) {
            LOGGER.info("Join a channel before queueing audio")
            return
        }

        val player = AudioPlayer.getAudioPlayerForGuild(guild)
        player.queue(audio)
        LOGGER.debug("Queued audio $audio for playing on ${guild.name}")
    }

    override fun queueLeaveOnFinished(guild: IGuild) {
        if (guild !in joinedGuilds) {
            LOGGER.info("Guild ${guild.name} is not joined")
            return
        }

        if (guildLeaveThreads[guild] != null) {
            LOGGER.info("Guild ${guild.name} is already queued for leaving")
            return
        }

        val player = AudioPlayer.getAudioPlayerForGuild(guild)
        if (player.currentTrack == null) {
            LOGGER.info("Leaving guild ${guild.name} because no song is playing")
            guild.connectedVoiceChannel?.leave()
            return
        }

        val thread = object : Thread() {
            override fun run() {
                do {
                    val track = player.currentTrack
                    if (track == null) {
                        LOGGER.debug("Player for guild ${guild.name} has stopped playing, leaving channel")

                        guild.connectedVoiceChannel?.leave()
                        joinedGuilds.remove(guild)
                        guildLeaveThreads.remove(guild)
                        return
                    }
                    Thread.sleep(500)
                } while (!interrupted())
            }
        }

        guildLeaveThreads[guild] = thread
        thread.start()
        LOGGER.debug("Started watching audio player for guild ${guild.name} for leaving")
    }

    override fun forceLeave(guild: IGuild) {
        if (guild !in joinedGuilds) {
            LOGGER.debug("Bot has not joined guild ${guild.name}, not leaving")
            return
        }

        joinedGuilds.remove(guild)
        guildLeaveThreads[guild]?.interrupt()
        guildLeaveThreads.remove(guild)

        guild.connectedVoiceChannel?.leave()
    }

    override fun forceLeaveAll() {
        joinedGuilds.forEach(this::forceLeave)
    }
}