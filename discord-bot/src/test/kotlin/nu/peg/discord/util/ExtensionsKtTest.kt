package nu.peg.discord.util

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ExtensionsKtTest {
    @Test
    fun `fromHex works with prefix`() {
        val color = fromHex("#FFFFFF")

        assertThat(color).extracting("red", "green", "blue")
                .containsExactly(255, 255, 255)
    }

    @Test
    fun `fromHex works without prefix`() {
        val color = fromHex("FFFFFF")

        assertThat(color).extracting("red", "green", "blue")
                .containsExactly(255, 255, 255)
    }

    @Test
    fun `fromHex works with three character hex codes`() {
        val color = fromHex("ABC")

        assertThat(color).extracting("red", "green", "blue")
                .containsExactly(170, 187, 204)
    }

    @Test
    fun `fromHex works with six character hex codes`() {
        val color = fromHex("AABBCC")

        assertThat(color).extracting("red", "green", "blue")
                .containsExactly(170, 187, 204)
    }

    @Test
    fun `fromHex works with lower case hex codes`() {
        val color = fromHex("aabbcc")

        assertThat(color).extracting("red", "green", "blue")
                .containsExactly(170, 187, 204)
    }
}