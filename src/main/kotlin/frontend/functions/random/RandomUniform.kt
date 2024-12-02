package org.poach3r.frontend.functions.random

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.classes.RandomFunc

class RandomUniform : RandomFunc() {
    override val arity: Int = 2

    override fun call(
        interpreter: Interpreter,
        arguments: List<Any>
    ): Any {
        val min = (arguments[0] as Double)
        val max = (arguments[1] as Double)
        return getRandomInstance().nextDouble(min, max)
    }
}