@file:Suppress("unused")

package nu.peg.discord.config

data class DiscordProperties(
        val api: DiscordApiProperties,
        val bot: DiscordBotProperties,
        val polr: PolrShortenerProperties
)

data class DiscordApiProperties(
        val clientId: String,
        val clientSecret: String
)

data class DiscordBotProperties(
        val token: String,
        val prefix: String,
        val version: String,
        val auditChannel: String?,
        val dbChannel: String?
)

data class PolrShortenerProperties(
        val baseUrl: String?,
        val apiKey: String?
)