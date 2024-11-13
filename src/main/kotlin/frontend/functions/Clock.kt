package org.poach3r.frontend.functions

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable

class Clock(
    override val arity: Int = 0
) : PCallable {
    override fun call(interpreter: Interpreter, args: List<Any>): Any {
        return System.currentTimeMillis().toDouble() / 1000
    }
}