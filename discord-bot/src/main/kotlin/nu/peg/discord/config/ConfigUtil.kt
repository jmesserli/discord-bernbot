package nu.peg.discord.config

import com.amihaiemil.eoyaml.Yaml
import com.amihaiemil.eoyaml.YamlMapping

class ConfigUtil {
    companion object {
        fun parseFromFile(path: String): DiscordProperties {
            return parseConfig(loadConfigYaml(path))
        }

        private fun loadConfigYaml(path: String): YamlMapping {
            val inStream = ConfigUtil::class.java.getResourceAsStream(path)
            return Yaml.createYamlInput(inStream).readYamlMapping()
        }

        private fun parseConfig(mapping: YamlMapping): DiscordProperties {
            return DiscordProperties(
                    DiscordApiProperties(
                            mapping.string("discord.api.client-id")!!,
                            mapping.string("discord.api.client-secret")!!
                    ),
                    DiscordBotProperties(
                            mapping.string("discord.bot.token")!!,
                            mapping.string("discord.bot.prefix") ?: "*",
                            mapping.string("discord.bot.version") ?: "2.DEV",
                            mapping.string("discord.bot.audit-channel"),
                            mapping.string("discord.bot.db-channel")
                    ),
                    PolrShortenerProperties(
                            mapping.string("discord.polr.base-url"),
                            mapping.string("discord.polr.api-key")
                    )
            )
        }
    }
}