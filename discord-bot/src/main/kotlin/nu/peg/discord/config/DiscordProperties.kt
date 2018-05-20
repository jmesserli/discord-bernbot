@file:Suppress("unused")

package nu.peg.discord.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty

@ConfigurationProperties("discord")
class DiscordProperties {
    @NestedConfigurationProperty
    var api: DiscordApiProperties? = null
    @NestedConfigurationProperty
    var bot: DiscordBotProperties? = null
    @NestedConfigurationProperty
    var polr: PolrShortenerProperties? = null
}

class DiscordApiProperties {
    var clientId: String? = null
    var clientSecret: String? = null
}

class DiscordBotProperties {
    var token: String? = null
    var prefix: String? = null
    var version: String? = null
    var auditChannel: String? = null
}

class PolrShortenerProperties {
    var baseUrl: String? = null
    var apiKey: String? = null
}