package nu.peg.discord.service

import nu.peg.discord.audit.AuditEventEmbed

interface AuditService {
    fun log(message: String)

    fun log(eventEmbed: AuditEventEmbed)
}