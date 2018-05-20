package nu.peg.discord.config

import nu.peg.discord.config.BeanNameRegistry.CLASSPATH_MODULE_LOADER
import nu.peg.discord.d4j.DisposableDiscordClient
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Profile
import org.springframework.web.client.RestTemplate
import sx.blah.discord.api.ClientBuilder
import javax.inject.Singleton

@Configuration
@EnableConfigurationProperties(DiscordProperties::class)
class DiscordBotConfig {
    @Bean
    @Singleton
    @Profile("!no-bot")
    @DependsOn(CLASSPATH_MODULE_LOADER)
    fun discordClient(discordProperties: DiscordProperties): DisposableDiscordClient {
        val builder = ClientBuilder()
                .withToken(discordProperties.bot?.token)
                .setMaxMessageCacheCount(512)

        return DisposableDiscordClient(builder.login())
    }

    @Bean
    fun restTemplate(): RestTemplate = RestTemplateBuilder()
            .setConnectTimeout(2000)
            .setReadTimeout(5000)
            .build()
}