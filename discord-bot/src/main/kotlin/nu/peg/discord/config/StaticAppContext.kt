package nu.peg.discord.config

import nu.peg.discord.util.getLogger
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

/**
 * @author Joel Messerli @15.02.2017
 */
@Component
object StaticAppContext : ApplicationContextAware {
    private val LOGGER = getLogger(StaticAppContext::class)

    lateinit var context: ApplicationContext

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        context = applicationContext
        LOGGER.info("ApplicationContext injected")
    }
}