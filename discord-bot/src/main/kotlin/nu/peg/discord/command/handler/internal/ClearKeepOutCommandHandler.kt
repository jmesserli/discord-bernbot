package nu.peg.discord.command.handler.internal

import nu.peg.discord.command.Command
import nu.peg.discord.command.handler.CommandHandler
import org.springframework.stereotype.Component

@Component
class ClearKeepOutCommandHandler : CommandHandler {
    override fun isAdminCommand() = true
    override fun getNames() = listOf("cko", "clearkeepout")
    override fun getDescription() = "Clear all \"keepouts\" (voice channel bans)"

    override fun handle(command: Command) {
        KeepOutEventHandler.clearBans()
        command.message.channel.sendMessage("Cleared voice channel bans")
    }
}