package org.poach3r.frontend.functions

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable

class Print(
    override val arity: Int = 1
): PCallable {
    override fun call(
        interpreter: Interpreter,
        arguments: List<Any>
    ): Any {
        with(interpreter.stringify(arguments[0])) {
            println(this)
            return this
        }
    }
}