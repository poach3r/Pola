package org.poach3r.frontend.functions.arrays

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable
import org.poach3r.frontend.classes.String

class Replace(
    override val arity: Int = 3
) : PCallable {
    override fun call(
        interpreter: Interpreter,
        arguments: List<Any>
    ): Any {
        val string = arguments[0] as kotlin.String
        val replacee = arguments[1].toString()
        val replacer = arguments[2].toString()

        return String().call(interpreter, listOf(string.replace(replacee, replacer)))
    }
}