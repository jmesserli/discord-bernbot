package nu.peg.discord.command.handler.internal

import nu.peg.discord.command.Command
import nu.peg.discord.command.handler.CommandHandler
import nu.peg.discord.event.EventListener
import nu.peg.discord.service.DatabaseEvent
import nu.peg.discord.service.internal.DefaultDatabaseService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent
import javax.inject.Inject

@Component
class KickCommand @Inject constructor(
        private val databaseService: DefaultDatabaseService
) : CommandHandler, EventListener<UserJoinEvent> {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(KickCommand::class.java)
    }

    override fun isAdminCommand() = true

    override fun getNames() = listOf("kickpr", "kpr")

    override fun getDescription() = "Kicks someone, preserving their roles"
    override fun handle(command: Command) {
        if (command.args.isEmpty()) return

        val guild = command.message.guild

        val targetName = command.args.joinToString(" ")
        val user = guild.users.firstOrNull { it.getDisplayName(guild).startsWith(targetName, true) }
                ?: return
        val roles = user.getRolesForGuild(guild)
        val event = RolesDbEvent(roles.map { Role(it.stringID, it.name) }, User(user.stringID, user.getDisplayName(guild)))

        databaseService.writeDatabaseEvent(DatabaseEvent(event, event::class))
        guild.kickUser(user, "Du wurdest gekickt ¯\\_(ツ)_/¯, aber deine Rollen wurden gespeichert. Wenn du wiederkommst werde ich sie dir wieder geben.")
    }

    override fun onEvent(event: UserJoinEvent) {
        val roleEvent = databaseService.seekDatabaseEvents(RolesDbEvent::class).asSequence()
                .firstOrNull { it.event.user.userId == event.user.stringID } ?: return

        val roles = event.guild.roles
        val displayName = event.user.getDisplayName(event.guild)
        roles.filter { guildRole -> roleEvent.event.roles.any { it.roleId == guildRole.stringID } }
                .forEach {
                    LOGGER.debug("Re-adding role ${it.name} to $displayName")
                    event.user.addRole(it)
                }
    }

    override fun getEventClass() = UserJoinEvent::class

    data class RolesDbEvent(val roles: List<Role>, val user: User)
    data class Role(val roleId: String, val roleName: String)
    data class User(val userId: String, val userDisplayName: String)
}