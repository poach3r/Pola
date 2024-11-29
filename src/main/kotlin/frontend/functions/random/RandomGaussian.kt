package org.poach3r.frontend.functions.random

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.classes.RandomFunc

class RandomGaussian : RandomFunc() {
    override val arity: Int = 2

    override fun call(
        interpreter: Interpreter,
        arguments: List<Any>
    ): Any {
        val mean = (arguments[0] as Double)
        val stdDev = (arguments[1] as Double)

        // Box-Muller transform to generate Gaussian distributed number
        val u1 = getRandomInstance().nextDouble()
        val u2 = getRandomInstance().nextDouble()

        val z0 = Math.sqrt(-2.0 * Math.log(u1)) * Math.cos(2.0 * Math.PI * u2)

        return mean + stdDev * z0
    }
}