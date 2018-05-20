package nu.peg.discord.service.internal

import nu.peg.discord.config.ProfileRegistry
import nu.peg.discord.service.LinkShortenerService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import javax.inject.Inject

@Component
@Profile("!" + ProfileRegistry.NO_LINK_SHORTENER)
class PolrLinkShortenerService @Inject constructor(
        @Value("\${discord.polr.base-url}")
        private val baseUrl: String,
        @Value("\${discord.polr.api-key}")
        private val apiKey: String,
        private val restTemplate: RestTemplate
) : LinkShortenerService {
    override fun shorten(longLink: String): String? {
        val responseEntity = try {
            val url = UriComponentsBuilder.fromHttpUrl("$baseUrl/api/v2/action/shorten")
                    .queryParam("key", apiKey)
                    .queryParam("response_type", "json")
                    .queryParam("url", longLink)
                    .queryParam("is_secret", true)
                    .toUriString()

            restTemplate.getForEntity(url, ShortenerResponse::class.java)
        } catch (e: HttpClientErrorException) {
            println(e.responseBodyAsString)
            return null
        }

        return responseEntity.body?.result
    }

    data class ShortenerResponse(
            val action: String,
            val result: String?
    )
}