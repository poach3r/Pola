package org.poach3r.frontend.functions

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable

class Array(
    override val arity: Int = -1
): PCallable {
    override fun call(
        interpreter: Interpreter,
        arguments: List<Any>
    ): Any {
        return arguments
    }
}