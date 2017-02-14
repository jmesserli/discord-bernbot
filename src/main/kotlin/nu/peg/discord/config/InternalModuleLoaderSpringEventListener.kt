package nu.peg.discord.config

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationPreparedEvent
import org.springframework.context.ApplicationListener
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.modules.IModule
import sx.blah.discord.modules.ModuleLoader

class InternalModuleLoaderSpringEventListener : ApplicationListener<ApplicationPreparedEvent>, IModule {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(InternalModuleLoaderSpringEventListener::class.java)
    }

    override fun onApplicationEvent(event: ApplicationPreparedEvent?) {
        LOGGER.info("Loading modules")

        FastClasspathScanner().matchClassesImplementing(IModule::class.java) { matching ->
            LOGGER.info("Adding module class ${matching.canonicalName}")
            ModuleLoader.addModuleClass(matching)
        }.scan()
    }

    override fun enable(client: IDiscordClient?): Boolean = true

    override fun getName(): String = "Internal Module Loader"

    override fun getVersion(): String = "1.0.0"

    override fun getMinimumDiscord4JVersion(): String = "1.7"

    override fun getAuthor(): String = "Joel Messerli <hi.github@peg.nu>"

    override fun disable() {}
}