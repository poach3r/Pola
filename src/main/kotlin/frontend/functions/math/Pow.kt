package frontend.functions.math

import org.poach3r.errors.RuntimeError
import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable
import kotlin.math.pow

class Pow(
    override val arity: Int = 2
): PCallable {
    override fun call(
        interpreter: Interpreter,
        arguments: List<Any>
    ): Any {
        if(arguments[0] !is Double)
            throw RuntimeError(
                msg = "Argument 1 '${arguments[0]}' is not a number."
            )

        if(arguments[1] !is Double)
            throw RuntimeError(
                msg = "Argument 2 '${arguments[1]}' is not a number."
            )

        return (arguments[0] as Double).pow(arguments[1] as Double)
    }
}