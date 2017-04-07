package nu.peg.discord.collector

import java.io.Serializable

/**
 * TODO Short summary
 *
 * @author Joel Messerli @03.04.2017
 */
interface CollectorJob : Serializable {
    fun start()
    fun stop()
}