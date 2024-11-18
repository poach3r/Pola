package frontend.functions.errors

import org.poach3r.errors.RuntimeError
import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable

class Throw(
    override val arity: Int = 1
): PCallable {
    override fun call(interpreter: Interpreter, arguments: List<Any>): Any {
        throw Error(arguments[0].toString())
    }
}