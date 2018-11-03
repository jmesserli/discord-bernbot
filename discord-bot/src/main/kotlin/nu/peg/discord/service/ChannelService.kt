package nu.peg.discord.service

import sx.blah.discord.handle.obj.IChannel

interface ChannelService {
    fun findByName(name: String): IChannel?
}