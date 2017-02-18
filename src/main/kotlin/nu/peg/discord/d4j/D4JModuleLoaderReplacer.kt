package nu.peg.discord.d4j

import net.bytebuddy.ByteBuddy
import net.bytebuddy.dynamic.ClassFileLocator
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy
import net.bytebuddy.implementation.MethodDelegation
import net.bytebuddy.implementation.SuperMethodCall
import net.bytebuddy.implementation.bind.annotation.*
import net.bytebuddy.matcher.ElementMatchers
import net.bytebuddy.pool.TypePool
import nu.peg.discord.config.BeanNameRegistry.STATIC_APP_CONTEXT
import nu.peg.discord.config.StaticAppContext
import nu.peg.discord.util.getLogger
import org.springframework.beans.BeansException
import org.springframework.beans.factory.config.AutowireCapableBeanFactory
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.modules.Configuration
import sx.blah.discord.modules.IModule
import sx.blah.discord.modules.ModuleLoader
import java.lang.reflect.Constructor
import java.util.ArrayList
import javax.annotation.PostConstruct

/**
 * TODO Short summary
 *
 * @author Joel Messerli @15.02.2017
 */
@Component @DependsOn(STATIC_APP_CONTEXT)
class D4JModuleLoaderReplacer : IModule {
    companion object {
        private val LOGGER = getLogger(D4JModuleLoaderReplacer::class)
    }

    @PostConstruct
    fun replaceModuleLoader() {
        val pool = TypePool.Default.ofClassPath()

        ByteBuddy().rebase<Any>(
                pool.describe("sx.blah.discord.modules.ModuleLoader").resolve(), ClassFileLocator.ForClassLoader.ofClassPath()
        ).constructor(
                ElementMatchers.any()
        ).intercept(
                SuperMethodCall.INSTANCE.andThen(MethodDelegation.to(pool.describe("nu.peg.discord.d4j.SpringInjectingModuleLoaderInterceptor").resolve()))
        ).make().load(ClassLoader.getSystemClassLoader(), ClassLoadingStrategy.Default.INJECTION)

        LOGGER.info("The D4J ModuleLoader has been replaced with ByteBuddy to allow for Spring injection")
    }

    override fun getName() = "Spring Injecting Module Loader"
    override fun enable(client: IDiscordClient?) = true
    override fun getVersion() = "1.0.0"
    override fun getMinimumDiscord4JVersion() = "1.7"
    override fun getAuthor() = "Joel Messerli <hi.github@peg.nu>"
    override fun disable() {}
}

class SpringInjectingModuleLoaderInterceptor {
    companion object {
        private val LOGGER = getLogger(SpringInjectingModuleLoaderInterceptor::class)

        @Suppress("UNCHECKED_CAST")
        @JvmStatic
        fun <T> intercept(
                @This loader: ModuleLoader,
                @Origin ctor: Constructor<T>,
                @Argument(0) discordClient: IDiscordClient?,

                @FieldValue("modules") modules: List<Class<out IModule>>,
                @FieldValue("loadedModules") loadedModules: MutableList<IModule>
        ) {
            LOGGER.debug("Intercepting $ctor")
            val loaderClass = loader.javaClass
            val clientField = loaderClass.getDeclaredField("client")
            clientField.isAccessible = true
            clientField.set(loader, discordClient)

            val canModuleLoadMethod = loaderClass.getDeclaredMethod("canModuleLoad", IModule::class.java)
            canModuleLoadMethod.isAccessible = true

            val factory = StaticAppContext.context.autowireCapableBeanFactory
            for (moduleClass in modules) {
                try {
                    val wired = factory.autowire(moduleClass, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false) as IModule
                    LOGGER.info("Loading autowired module {}@{} by {}", wired.name, wired.version, wired.author)
                    if (canModuleLoadMethod.invoke(loader, wired) as Boolean) {
                        loadedModules.add(wired)
                    } else {
                        LOGGER.info("${wired.name} needs at least version ${wired.minimumDiscord4JVersion} to be loaded (skipped)")
                    }
                } catch (e: BeansException) {
                    LOGGER.info("Spring could not create bean", e)
                }
            }

            if (Configuration.AUTOMATICALLY_ENABLE_MODULES) { // Handles module load order and loads the modules
                val toLoad = ArrayList<IModule>(loadedModules)

                val loadModuleMethod = loaderClass.getDeclaredMethod("loadModule", IModule::class.java)
                while (toLoad.size > 0) {
                    toLoad.filter { loadModuleMethod.invoke(loader, it) as Boolean }.forEach { toLoad.remove(it) }
                }
            }
            LOGGER.info("Module loading complete")
        }
    }
}