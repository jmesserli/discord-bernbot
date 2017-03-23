package nu.peg.discord.command.handler.internal

import nu.peg.discord.command.Command
import nu.peg.discord.command.handler.CommandHandler
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.LocalDateTime

/**
 * TODO Short summary
 *
 * @author Joel Messerli @23.03.2017
 */
@Component
class PingCommandHandler : CommandHandler {
    override fun isApplicable(command: Command): Boolean {
        return command.getName().equals("ping", true)
    }

    override fun handle(command: Command) {
        val message = command.getMessage()

        val now = LocalDateTime.now()
        val duration = Duration.between(message.timestamp, now)
        message.channel.sendMessage("Pong! `${duration.toMillis()} ms`")
    }
}