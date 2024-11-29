package org.poach3r.frontend.functions.math

import org.poach3r.errors.RuntimeError
import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable
import kotlin.math.cbrt

class Cbrt(
    override val arity: Int = 1
): PCallable {
    override fun call(interpreter: Interpreter, arguments: List<Any>): Any {
        if(arguments[0] !is Double)
            throw RuntimeError(
                msg = "Argument 1 '${arguments[0]}' is not a number."
            )

        return cbrt(arguments[0] as Double)
    }
}