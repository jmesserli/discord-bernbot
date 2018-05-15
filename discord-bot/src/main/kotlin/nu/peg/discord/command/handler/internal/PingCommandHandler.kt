package nu.peg.discord.command.handler.internal

import nu.peg.discord.command.Command
import nu.peg.discord.command.handler.CommandHandler
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.LocalDateTime

@Component
class PingCommandHandler : CommandHandler {
    override fun isAdminCommand() = false
    override fun getNames() = listOf("ping")
    override fun getDescription() = "🏓 Replies pong"

    override fun handle(command: Command) {
        val message = command.message

        val now = LocalDateTime.now()
        val duration = Duration.between(message.timestamp, now)
        message.channel.sendMessage("Pong! `${duration.toMillis()} ms`")
    }
}