package org.poach3r.frontend.functions

import org.poach3r.errors.BreakError
import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable

class Break(
    override val arity: Int = 0
) : PCallable {
    override fun call(
        interpreter: Interpreter,
        arguments: List<Any>
    ): Any {
        throw BreakError()
    }
}