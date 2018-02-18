package nu.peg.discord.module

import sx.blah.discord.api.IDiscordClient

/**
 * TODO Short summary
 *
 * @author Joel Messerli @23.03.2017
 */
interface BaernModule {
    fun enable(client: IDiscordClient): Boolean
}