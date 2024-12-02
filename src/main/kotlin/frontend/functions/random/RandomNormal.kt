package org.poach3r.frontend.functions.random

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.classes.RandomFunc

class RandomNormal : RandomFunc() {
    override val arity: Int = 2

    override fun call(
        interpreter: Interpreter,
        arguments: List<Any>
    ): Any {
        val mean = (arguments[0] as Double)
        val stdDev = (arguments[1] as Double)
        return mean + stdDev * getRandomInstance().nextDouble(-3.0, 3.0)
    }
}