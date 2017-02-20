package nu.peg.discord.config

import nu.peg.discord.config.BeanNameRegistry.CLASSPATH_MODULE_LOADER
import nu.peg.discord.d4j.DisposableDiscordClient
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.*
import sx.blah.discord.api.ClientBuilder
import javax.inject.Singleton

@Configuration
@EnableConfigurationProperties(DiscordProperties::class)
class DiscordBotConfig {
    @Bean @Singleton @Profile("!no-bot") @DependsOn(CLASSPATH_MODULE_LOADER)
    fun discordClient(discordProperties: DiscordProperties): DisposableDiscordClient {
        val builder = ClientBuilder()
        builder.withToken(discordProperties.bot!!.token!!)

        return DisposableDiscordClient(builder.login())
    }
}