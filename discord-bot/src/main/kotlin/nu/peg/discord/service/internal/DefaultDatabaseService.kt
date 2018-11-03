package nu.peg.discord.service.internal

import com.fasterxml.jackson.databind.ObjectMapper
import nu.peg.discord.command.handler.internal.KickCommand
import nu.peg.discord.config.DiscordProperties
import nu.peg.discord.service.DatabaseEvent
import nu.peg.discord.service.DatabaseService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IMessage
import java.nio.charset.Charset
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject
import kotlin.reflect.KClass

@Service
class DefaultDatabaseService @Inject constructor(
        private val messageSendService: DefaultMessageSendService,
        private val messageReadService: DefaultMessageReadService,
        private val channelService: DefaultChannelService,
        private val discordProperties: DiscordProperties,
        private val objectMapper: ObjectMapper
) : DatabaseService {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(DefaultDatabaseService::class.java)
    }

    init {
        Thread {
            Thread.sleep(10000)

            seekDatabaseEvents(KickCommand.RolesDbEvent::class).forEach(::println)
        }.start()
    }

    private val dbChannel: IChannel? by lazy {
        if (discordProperties.bot == null || discordProperties.bot!!.dbChannel == null) null
        else channelService.findByName(discordProperties.bot!!.dbChannel!!)
    }

    override fun writeDatabaseEvent(event: DatabaseEvent<*>) {
        if (dbChannel == null) {
            LOGGER.info("Channel not set, not writing database event")
            return
        }

        val bytes = objectMapper.writeValueAsBytes(event)
        val base64String = Base64.getEncoder().encode(bytes)
        val string = String(base64String, Charset.forName("UTF-8"))

        messageSendService.send(dbChannel!!, string)
    }

    override fun <T : Any> seekDatabaseEvents(eventType: KClass<T>): Iterator<DatabaseEvent<T>> {
        if (dbChannel == null) {
            LOGGER.info("Channel not set, not writing database event")
            return emptyList<DatabaseEvent<T>>().iterator()
        }

        return messageReadService.readMessages(dbChannel!!, LocalDateTime.now()).asSequence()
                .map { tryParse(it, eventType) }.filterNotNull()
                .iterator()
    }

    private fun <T : Any> tryParse(message: IMessage, eventType: KClass<T>): DatabaseEvent<T>? {
        val content = message.content

        return try {
            val jsonBytes = Base64.getDecoder().decode(content)

            val type = objectMapper.typeFactory.constructParametricType(DatabaseEvent::class.java, eventType.java)
            objectMapper.readValue<DatabaseEvent<T>>(jsonBytes, type)
        } catch (ex: Exception) {
            LOGGER.info("Exception caught while deserializing dbEvent", ex)
            null
        }
    }

}

