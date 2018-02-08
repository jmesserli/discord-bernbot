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
}

class DiscordApiProperties {
    var clientId: String? = null
    var clientSecret: String? = null
}

class DiscordBotProperties {
    var token: String? = null
    var prefix: String? = null
    var version: String? = null
}