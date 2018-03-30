package nu.peg.discord.command.handler.internal

import nu.peg.discord.command.Command
import nu.peg.discord.command.handler.CommandHandler
import nu.peg.discord.service.OnlineStatus
import nu.peg.discord.service.StatusService
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class StatusMessageCommand @Inject constructor(
        private val statusService: StatusService
) : CommandHandler {
    override fun isAdminCommand() = true
    override fun getNames() = listOf("sm", "statusmessage")

    override fun handle(command: Command) {
        val args = command.args
        val channel = command.message.channel

        val status = if (args.isNotEmpty()) {
            OnlineStatus.fromText(args.first())
        } else null

        if (status == null) {
            channel.sendMessage("Usage: ${command.name} <online | afk | dnd> [message]")
            return
        }

        val message = if (args.size > 1) {
            args.sliceArray(1 until args.size).joinToString(" ")
        } else null

        statusService.setStatus(status, message)
    }
}