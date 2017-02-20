package nu.peg.discord.command.internal

import nu.peg.discord.command.Command
import nu.peg.discord.command.CommandParser
import nu.peg.discord.config.DiscordProperties
import org.springframework.stereotype.Component
import sx.blah.discord.handle.obj.IMessage
import java.util.ArrayList
import javax.inject.Inject

/**
 * Parses a command that contains some prefix
 *
 * @author Joel Messerli @20.02.2017
 */
@Component
class PrefixCommandParser @Inject constructor(discordProperties: DiscordProperties) : CommandParser {
    private val prefix = discordProperties.bot!!.prefix!!
    private val parsingRegex = Regex("\"([^\"]*)\"|(\\S+)")

    override fun parse(message: IMessage): Command? {
        val commandString = message.content
        if (commandString == null || !commandString.startsWith(prefix)) return null

        val noPrefix = commandString.substring(prefix.length)
        val firstSpace = noPrefix.indexOfFirst { it == ' ' }

        var commandName = noPrefix.substring(0, if (firstSpace != -1) firstSpace else noPrefix.length)
        var args = arrayOf<String>()
        if (firstSpace != -1) {
            args = parsingRegex.findAll(noPrefix, firstSpace).map {
                val groups = ArrayList(it.groups).map { it!! }.filterIndexed { i, group -> i > 0 }
                val group = if (groups[0] != null) groups[0] else groups[1]
                return@map group.value
            }.toList().toTypedArray()
        }

        return BasicCommand(commandName, args, message)
    }
}