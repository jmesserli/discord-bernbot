package nu.peg.discord.slave

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class DiscordSlaveApplication

fun main(args: Array<String>) {
    SpringApplication.run(DiscordSlaveApplication::class.java, *args)
}
