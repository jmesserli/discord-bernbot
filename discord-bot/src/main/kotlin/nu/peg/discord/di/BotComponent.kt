package nu.peg.discord.di

import dagger.Component
import discord4j.core.DiscordClient
import nu.peg.discord.config.DiscordProperties
import nu.peg.discord.module.internal.BaernBotModule

@Component
interface BotComponent {
    fun inject(client: DiscordClient, properties: DiscordProperties): BaernBotModule
}