package nu.peg.discord.command.handler.internal

import nu.peg.discord.command.Command
import nu.peg.discord.command.handler.CommandHandler
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class HelpCommandHandler : CommandHandler {
    @Inject
    private lateinit var commands: List<CommandHandler>

    override fun isAdminCommand() = false

    override fun getNames() = listOf("help", "h", "?", "commands")

    override fun getDescription() = "Shows a list of commands"

    override fun handle(command: Command) {
        val commandString = commands
                .sortedBy { it.getNames().sortedByDescending { it.length }.first() }
                .sortedBy { it.isAdminCommand() }
                .map {
                    """
                    |${it.getNames().joinToString(", ")}:
                    |${it.getDescription()}
                    |${if (it.isAdminCommand()) "(Admin only)\n" else ""}
                    """.trimMargin()
                }
                .joinToString("\n")

        command.message.reply("Commands:\n$commandString")
    }
}