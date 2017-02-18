package nu.peg.discord.config

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner
import nu.peg.discord.config.BeanNameRegistry.MODULE_LOADER_PROXY
import nu.peg.discord.util.getLogger
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.modules.IModule
import sx.blah.discord.modules.ModuleLoader
import javax.annotation.PostConstruct

@Component @DependsOn(MODULE_LOADER_PROXY)
class FastD4JClasspathModuleLoader : IModule {
    companion object {
        private val LOGGER = getLogger(FastD4JClasspathModuleLoader::class)
    }

    @PostConstruct
    fun loadModules() {
        LOGGER.info("Loading modules")

        FastClasspathScanner().matchClassesImplementing(IModule::class.java) { matching ->
            LOGGER.info("Adding module class ${matching.canonicalName}")
            ModuleLoader.addModuleClass(matching)
        }.scan()
    }

    override fun enable(client: IDiscordClient?): Boolean = true
    override fun getName(): String = "Fast D4J Classpath Module Loader"
    override fun getVersion(): String = "1.1.0"
    override fun getMinimumDiscord4JVersion(): String = "1.7"
    override fun getAuthor(): String = "Joel Messerli <hi.github@peg.nu>"
    override fun disable() {}
}