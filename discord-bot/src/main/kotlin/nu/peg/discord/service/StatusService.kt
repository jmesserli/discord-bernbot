package nu.peg.discord.service

interface StatusService {
    fun setStatus(onlineStatus: OnlineStatus = OnlineStatus.ONLINE, message: String? = null)
}