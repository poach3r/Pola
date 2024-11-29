package org.poach3r.frontend.functions.random

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.classes.RandomFunc

class RandomTriangular : RandomFunc() {
    override val arity: Int = 3

    override fun call(
        interpreter: Interpreter,
        arguments: List<Any>
    ): Any {
        val low = (arguments[0] as Double)
        val high = (arguments[1] as Double)
        val mode = (arguments[2] as Double)

        val u = getRandomInstance().nextDouble()
        return if (u <= (mode - low) / (high - low)) {
            low + Math.sqrt(u * (high - low) * (mode - low))
        } else {
            high - Math.sqrt((1 - u) * (high - low) * (high - mode))
        }
    }
}
