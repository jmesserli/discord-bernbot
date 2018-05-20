package nu.peg.discord.service

interface LinkShortenerService {
    fun shorten(longLink: String): String?
}