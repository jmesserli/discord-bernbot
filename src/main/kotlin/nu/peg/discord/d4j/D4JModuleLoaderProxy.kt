package nu.peg.discord.d4j

import net.bytebuddy.ByteBuddy
import net.bytebuddy.NamingStrategy
import net.bytebuddy.dynamic.ClassFileLocator
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy
import net.bytebuddy.implementation.MethodDelegation
import net.bytebuddy.implementation.SuperMethodCall
import net.bytebuddy.implementation.bind.annotation.Argument
import net.bytebuddy.implementation.bind.annotation.Origin
import net.bytebuddy.implementation.bind.annotation.This
import net.bytebuddy.matcher.ElementMatchers
import net.bytebuddy.pool.TypePool
import nu.peg.discord.util.getLogger
import org.springframework.stereotype.Component
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.modules.ModuleLoader
import java.lang.reflect.Constructor
import javax.annotation.PostConstruct

/**
 * TODO Short summary
 *
 * @author Joel Messerli @15.02.2017
 */
@Component
class D4JModuleLoaderProxy {
    companion object {
        private val LOGGER = getLogger(D4JModuleLoaderProxy::class)
    }

    @PostConstruct
    fun test() {
        LOGGER.info("Rebasing D4J ModuleLoader to inject spring dependencies into modules")
        val buddy = ByteBuddy().with(NamingStrategy.SuffixingRandom("Bern"))
        val pool = TypePool.Default.ofClassPath()

        buddy.rebase<Any>(
                pool.describe("sx.blah.discord.modules.ModuleLoader").resolve(), ClassFileLocator.ForClassLoader.ofClassPath()
        ).constructor(
                ElementMatchers.any()
        ).intercept(
                SuperMethodCall.INSTANCE.andThen(MethodDelegation.to(pool.describe("nu.peg.discord.d4j.ModuleLoaderDelegateKt.Companion").resolve()))
        ).make().load(ClassLoader.getSystemClassLoader(), ClassLoadingStrategy.Default.INJECTION)

        val newLoader = ModuleLoader(null)
        LOGGER.info("The D4J ModuleLoader has been replaced with this one: ${newLoader.javaClass.canonicalName}")
    }
}

class ModuleLoaderDelegate {
    companion object {
        private val LOGGER = getLogger(ModuleLoaderDelegate::class)

        @JvmStatic fun <T> intercept(@Origin ctor: Constructor<T>, @Argument(0) discordClient: IDiscordClient, @This loader: ModuleLoader) {
            LOGGER.info("Intercepting $ctor")
        }
    }
}