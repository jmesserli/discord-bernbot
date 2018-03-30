package nu.peg.discord.service

enum class OnlineStatus {
    ONLINE, DND, AFK;

    companion object {
        fun fromText(text: String) =
                values().firstOrNull { it.name.equals(text, true) }
    }
}