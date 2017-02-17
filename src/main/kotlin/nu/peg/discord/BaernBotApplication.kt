package nu.peg.discord

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

@SpringBootApplication
class BaernBotApplication

fun main(args: Array<String>) {
    SpringApplicationBuilder()
//            .listeners(FastD4JClasspathModuleLoader())
            .sources(BaernBotApplication::class.java)
            .run(*args)
}
