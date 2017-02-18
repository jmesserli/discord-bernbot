package nu.peg.discord

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class BaernBotApplication

fun main(args: Array<String>) {
    SpringApplication.run(BaernBotApplication::class.java, *args)
}
