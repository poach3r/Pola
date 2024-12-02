package org.poach3r.frontend.functions.random

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.classes.RandomFunc
import java.util.UUID

class RandomUUID : RandomFunc() {
    override val arity: Int = 0 // No arguments are required for now...

    override fun call(interpreter: Interpreter, arguments: List<Any>): Any {
        return UUID.randomUUID().toString()
    }
}
