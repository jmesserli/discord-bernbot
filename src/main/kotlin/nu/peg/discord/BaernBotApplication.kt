package nu.peg.discord

import nu.peg.discord.config.InternalModuleLoaderSpringEventListener
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

@SpringBootApplication
class BaernBotApplication

fun main(args: Array<String>) {
    SpringApplicationBuilder()
            .listeners(InternalModuleLoaderSpringEventListener())
            .sources(BaernBotApplication::class.java)
            .run(*args)
}
