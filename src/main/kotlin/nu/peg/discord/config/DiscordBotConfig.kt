package nu.peg.discord.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import sx.blah.discord.api.ClientBuilder
import javax.inject.Singleton

@Configuration
@EnableConfigurationProperties(DiscordProperties::class)
class DiscordBotConfig {

    @Bean
    @Singleton
    fun discordClient(discordProperties: DiscordProperties): DisposableDiscordClient {
        val builder = ClientBuilder()
        builder.withToken(discordProperties.bot!!.token)

        return DisposableDiscordClient(builder.login())
    }
}