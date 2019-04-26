package nu.peg.discord.module

import discord4j.core.DiscordClient


/**
 * TODO Short summary
 *
 * @author Joel Messerli @23.03.2017
 */
interface BaernModule {
    fun enable(client: DiscordClient): Boolean
}