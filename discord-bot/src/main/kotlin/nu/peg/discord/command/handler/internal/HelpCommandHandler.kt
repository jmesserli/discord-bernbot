package nu.peg.discord.command.handler.internal

import discord4j.core.spec.EmbedCreateSpec
import nu.peg.discord.command.Command
import nu.peg.discord.command.handler.CommandHandler
import nu.peg.discord.message.EmbedColors
import nu.peg.discord.service.MessageSendService
import nu.peg.discord.util.init
import javax.inject.Inject

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
                    val key = "${it.getNames().joinToString(", ")}${getAdminString(it)}"
                    fields[key] = it.getDescription()
                }

        val embedSpec = EmbedCreateSpec().init {
            setColor(EmbedColors.LIGHT_BLUE)
            setFooter("Help", "https://cdn.peg.nu/files/resources/icons/material_help.png")
            setDescription("Command list")
            setTitle("List of all available commands:")

            fields.forEach { addField(it.key, it.value, false) }
        }

        command.message.channel.subscribe { messageSendService.send(it, embedSpec) }
    }

    private fun getAdminString(command: CommandHandler): String {
        if (command.isAdminCommand()) {
            return " \uD83D\uDCE2";
        }

        return ""
    }
}