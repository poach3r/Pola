package org.poach3r.frontend.functions.standardLibrary.arrayLibrary

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable
import org.poach3r.frontend.classes.StandardLibrary
import org.poach3r.frontend.classes.Strings

class Replace(
    override val arity: Int = 3
): PCallable {
    override fun call(
        interpreter: Interpreter,
        arguments: List<Any>
    ): Any {
        val string = arguments[0] as String
        val replacee = arguments[1].toString()
        val replacer = arguments[2].toString()

        return Strings().call(interpreter, listOf(string.replace(replacee, replacer)))
    }
}