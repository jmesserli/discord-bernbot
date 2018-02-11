package nu.peg.discord.command.internal

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import sx.blah.discord.handle.obj.IMessage
import javax.inject.Inject

/**
 * TODO Short summary
 * @author Joel Messerli @23.03.2017
 */
@SpringBootTest
@RunWith(SpringRunner::class)
@ActiveProfiles("no-bot")
class PrefixCommandParserTest {
    @Inject
    lateinit var parser: PrefixCommandParser
    @Value("\${discord.bot.prefix:.}")
    lateinit var prefix: String

    @Test
    fun parseNoArgCommand() {
        val noArgMessage = makeMessage("${prefix}name")
        val noArgCommand = parser.parse(noArgMessage)!!
        assertThat(noArgCommand.name).isEqualTo("name")
        assertThat(noArgCommand.args).isNotNull().isEmpty()
    }

    @Test
    fun parseTwoArgMessageNotQuoted() {
        val twoArgMessage = makeMessage("${prefix}name arg1 arg2")
        val twoArgCommand = parser.parse(twoArgMessage)!!
        assertThat(twoArgCommand.name).isEqualTo("name")
        assertThat(twoArgCommand.args).containsExactly("arg1", "arg2")
    }

    @Test
    fun parseTwoArgMessageQuoted() {
        val twoArgMessageQuoted = makeMessage("${prefix}name \"arg1 still\" arg2")
        val twoArgCommandQuoted = parser.parse(twoArgMessageQuoted)!!
        assertThat(twoArgCommandQuoted.name).isEqualTo("name")
        assertThat(twoArgCommandQuoted.args).containsExactly("arg1 still", "arg2")
    }

    fun makeMessage(content: String): IMessage {
        val message = mock(IMessage::class.java)
        `when`(message.content).thenReturn(content)
        return message
    }
}