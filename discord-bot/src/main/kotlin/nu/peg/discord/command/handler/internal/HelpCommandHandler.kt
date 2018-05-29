package nu.peg.discord.command.handler.internal

import nu.peg.discord.command.Command
import nu.peg.discord.command.handler.CommandHandler
import nu.peg.discord.message.BasicEmbed
import nu.peg.discord.message.EmbedColors
import nu.peg.discord.service.MessageSendService
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class HelpCommandHandler @Inject constructor(
        private val messageSendService: MessageSendService
) : CommandHandler {
    @Inject
    private lateinit var commands: List<CommandHandler>

    override fun isAdminCommand() = false
    override fun getNames() = listOf("help", "h", "?", "commands")
    override fun getDescription() = "Shows a list of commands"

    override fun handle(command: Command) {
        val fields: MutableMap<String, String> = mutableMapOf()

        commands.sortedBy { it.getNames().sortedByDescending { it.length }.first() }
                .sortedBy { it.isAdminCommand() }
                .onEach {
                    fields.put("${it.getNames().joinToString(", ")}${getAdminString(it)}", it.getDescription())
                }

        val embed = BasicEmbed(EmbedColors.LIGHT_BLUE, "List of all available commands:", "Command list", fields).withHelpFooter()

        messageSendService.send(command.message.channel, embed)
    }

    private fun getAdminString(command: CommandHandler): String {
        if (command.isAdminCommand()) {
            return " \uD83D\uDCE2";
        }

        return ""
    }
}