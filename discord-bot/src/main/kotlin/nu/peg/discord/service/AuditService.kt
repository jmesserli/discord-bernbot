package nu.peg.discord.service

interface AuditService {
    fun log(message: String)

    fun log(eventEmbed: AuditEventEmbed)
}