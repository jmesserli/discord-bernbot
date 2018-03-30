package nu.peg.discord.util

import sx.blah.discord.api.IDiscordClient

interface DiscordClientListener {
    fun discordClientAvailable(client: IDiscordClient)
}