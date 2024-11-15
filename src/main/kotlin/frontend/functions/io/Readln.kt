package org.poach3r.frontend.functions.io

import org.poach3r.frontend.Interpreter

class Readln(
    override val arity: Int = 0
): IOFunc {
    override fun call(
        interpreter: Interpreter,
        arguments: List<Any>
    ): Any {
        return getVal(interpreter, readln())
    }
}