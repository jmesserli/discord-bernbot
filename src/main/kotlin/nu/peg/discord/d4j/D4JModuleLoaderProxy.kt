package nu.peg.discord.d4j

import net.bytebuddy.ByteBuddy
import net.bytebuddy.NamingStrategy
import net.bytebuddy.asm.Advice
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy
import net.bytebuddy.implementation.MethodDelegation
import net.bytebuddy.implementation.bind.annotation.Argument
import net.bytebuddy.implementation.bind.annotation.This
import net.bytebuddy.matcher.ElementMatchers.`is`
import nu.peg.discord.util.getLogger
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.modules.ModuleLoader
import java.lang.reflect.Constructor

/**
 * TODO Short summary
 *
 * @author Joel Messerli @15.02.2017
 */
class D4JModuleLoaderProxy {

    fun test() {
        val buddy = ByteBuddy().with(NamingStrategy.SuffixingRandom("Bern"))
        val moduleLoaderJavaClass = ModuleLoader::class.java
        val constructor = moduleLoaderJavaClass.constructors[0]

        val unloaded = buddy.redefine(moduleLoaderJavaClass)
                .constructor(`is`(constructor))
                .intercept(MethodDelegation.to(ModuleLoaderDelegate()))
                .make()

        val loaded = unloaded.load(D4JModuleLoaderProxy::class.java.classLoader, ClassLoadingStrategy.Default.CHILD_FIRST).loaded
    }
}

class ModuleLoaderDelegate {
    companion object {
        private val LOGGER = getLogger(ModuleLoaderDelegate::class)
    }

    fun <T> intercept(@Advice.Origin ctor: Constructor<T>, @Argument(0) discordClient: IDiscordClient, @This loader: ModuleLoader) {
        LOGGER.info("Intercepting $ctor")
    }
}