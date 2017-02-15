package nu.peg.discord.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("discord")
class DiscordProperties {
    var api: DiscordApiProperties? = null
    var bot: DiscordBotProperties? = null
}

class DiscordApiProperties {
    var clientId: String? = null
    var clientSecret: String? = null
}

class DiscordBotProperties {
    var token: String? = null
    var prefix: String? = null
}