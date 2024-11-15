package org.poach3r.frontend.functions.standardLibrary

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable

class Readln(
    override val arity: Int = 0
): PCallable {
    override fun call(
        interpreter: Interpreter,
        arguments: List<Any>
    ): Any {
        TODO("Not yet implemented")
    }
}