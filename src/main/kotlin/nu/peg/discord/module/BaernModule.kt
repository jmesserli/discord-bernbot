package nu.peg.discord.module

import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent

/**
 * TODO Short summary
 *
 * @author Joel Messerli @23.03.2017
 */
interface BaernModule : IListener<MessageReceivedEvent> {
    fun enable(client: IDiscordClient): Boolean
}