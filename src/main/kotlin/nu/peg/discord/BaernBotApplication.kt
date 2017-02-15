package nu.peg.discord

import nu.peg.discord.config.FastD4JClasspathModuleLoader
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

@SpringBootApplication
class BaernBotApplication

fun main(args: Array<String>) {
    SpringApplicationBuilder()
            .listeners(FastD4JClasspathModuleLoader())
            .sources(BaernBotApplication::class.java)
            .run(*args)
}
