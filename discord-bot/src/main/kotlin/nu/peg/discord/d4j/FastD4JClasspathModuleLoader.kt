package nu.peg.discord.d4j

import io.github.classgraph.ClassGraph
import nu.peg.discord.config.BeanNameRegistry.STATIC_APP_CONTEXT
import nu.peg.discord.util.getLogger
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.modules.IModule
import sx.blah.discord.modules.ModuleLoader
import javax.annotation.PostConstruct

@Component
@DependsOn(STATIC_APP_CONTEXT)
class FastD4JClasspathModuleLoader : IModule {
    companion object {
        private val LOGGER = getLogger(FastD4JClasspathModuleLoader::class)
    }

    @PostConstruct
    fun loadModules() {
        LOGGER.info("Loading modules")

        val result = ClassGraph().enableAllInfo().scan()
        val classes = result.getClassesImplementing(IModule::class.qualifiedName)
        classes.names.forEach {
            val clazz = Class.forName(it)
            LOGGER.info("Adding module class ${clazz.canonicalName}")

            @Suppress("UNCHECKED_CAST")
            ModuleLoader.addModuleClass(clazz as Class<out IModule>)
        }
    }

    override fun enable(client: IDiscordClient?): Boolean = true
    override fun getName(): String = "Fast D4J Classpath Module Loader"
    override fun getVersion(): String = "1.2.0"
    override fun getMinimumDiscord4JVersion(): String = "1.7"
    override fun getAuthor(): String = "Joel Messerli <hi.github@peg.nu>"
    override fun disable() {}
}