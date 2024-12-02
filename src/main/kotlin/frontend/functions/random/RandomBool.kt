package org.poach3r.frontend.functions.random

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.classes.RandomFunc

class RandomBool : RandomFunc() {
    override val arity: Int = 0

    override fun call(
        interpreter: Interpreter,
        arguments: List<Any>
    ): Any {
        return getRandomInstance().nextBoolean()
    }
}