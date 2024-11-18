package frontend.functions.errors

import org.poach3r.errors.PError
import org.poach3r.errors.RuntimeError
import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable
import org.poach3r.frontend.functions.DealsWithAnonymousFunctions
import kotlin.system.exitProcess

class Try(
    override val arity: Int = 2
) : DealsWithAnonymousFunctions {
    override fun call(
        interpreter: Interpreter,
        arguments: List<Any>
    ): Any {
        val tryFunc = getFunc(arguments, 0)
        val catchFunc = getFunc(arguments, 1)

        try {
            return tryFunc.call(interpreter, listOf())
        } catch(e: Error) {
            return catchFunc.call(interpreter, listOf(e.message) as List<Any>)
        }
    }
}