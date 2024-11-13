package org.poach3r.frontend.functions

import org.poach3r.errors.RuntimeError
import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable

class ForEach(
    override val arity: Int = 2
): PCallable {
    override fun call(
        interpreter: Interpreter,
        arguments: List<Any>
    ): Any {
        if(arguments[0] !is List<*>)
            throw RuntimeError(
                msg = "Argument #0 '${arguments[0]}' of 'foreach' isn't an array."
            )

        if(arguments[1] !is PFunction)
            throw RuntimeError(
                msg = "Argument #1 '${arguments[1]}' of 'foreach' isn't a function."
            )

        (arguments[0] as List<*>).forEachIndexed { index, argument ->
            (arguments[1] as PFunction).call(interpreter, listOf(argument, index) as List<Any>)
        }

        return 0
    }
}