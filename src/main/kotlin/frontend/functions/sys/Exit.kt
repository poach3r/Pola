package frontend.functions.sys

import org.poach3r.errors.RuntimeError
import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable
import kotlin.system.exitProcess

class Exit(
    override val arity: Int = 1
) : PCallable {
    override fun call(
        interpreter: Interpreter,
        arguments: List<Any>
    ): Any {
        if (arguments[0] !is Double)
            throw RuntimeError(
                msg = "Cannot exit program with value '${arguments[0]}' as it is not a number."
            )

        exitProcess((arguments[0] as Double).toInt())
    }
}