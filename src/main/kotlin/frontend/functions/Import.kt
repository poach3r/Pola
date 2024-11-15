package org.poach3r.frontend.functions

import org.poach3r.errors.RuntimeError
import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable
import org.poach3r.frontend.classes.StandardLibrary

class Import(
    override val arity: Int = 1
): PCallable {
    override fun call(
        interpreter: Interpreter,
        arguments: List<Any>
    ): Any {
        if(arguments[0] !is String)
            throw RuntimeError(
                msg = "Cannot import non-string value '${arguments[0]}'",
            )

        return when(arguments[0] as String) {
            "Standard" -> StandardLibrary().call(interpreter, listOf())
            else -> throw RuntimeError(
                msg= "Cannot find library '${arguments[0]}'.",
            )
        }
    }
}