package org.poach3r.frontend.functions.random

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.classes.RandomFunc

class RandomInt : RandomFunc() {
    override val arity: Int = 2

    override fun call(
        interpreter: Interpreter,
        arguments: List<Any>
    ): Any {
        val min = (arguments[0] as Double).toInt()
        val max = (arguments[1] as Double).toInt()
        return getRandomInstance().nextInt(min, max + 1).toDouble()
    }
}