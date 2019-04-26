package nu.peg.discord.command.internal

import nu.peg.discord.command.Command
import nu.peg.discord.command.CommandDispatcher
import nu.peg.discord.command.handler.CommandHandler
import nu.peg.discord.util.authorIsAdmin
import nu.peg.discord.util.containsIgnoreCase
import nu.peg.discord.util.getLogger
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import javax.inject.Inject

/**
 * An implementation for [CommandDispatcher] that extracts commands beginning with a prefix
 *
 * @author Joel Messerli @15.02.2017
 */
class DefaultCommandDispatcher @Inject constructor(private val handlers: List<CommandHandler>) : CommandDispatcher {
    companion object {
        private val LOGGER = getLogger(DefaultCommandDispatcher::class)
    }

    override fun dispatch(command: Command) {
        if (!command.message.author.isPresent || command.message.author.get().isBot) return

        Flux.fromIterable(handlers)
                .filterWhen { isHandlerApplicable(it, command) }
                .subscribe { it.handle(command) }

        LOGGER.info("Found no matching handler for command $command")
    }

    private fun isHandlerApplicable(handler: CommandHandler, command: Command): Mono<Boolean> {
        return command.message.authorIsAdmin().map { authorIsAdmin ->
            (!handler.isAdminCommand() || authorIsAdmin) && handler.getNames().containsIgnoreCase(command.name)
        }
    }
}
