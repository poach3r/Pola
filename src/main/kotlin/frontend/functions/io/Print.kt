package frontend.functions.standardLibrary

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable
import org.poach3r.frontend.classes.Strings
import org.poach3r.frontend.functions.io.IOFunc

class Print(
    override val arity: Int = -1
) : IOFunc {
    override fun call(
        interpreter: Interpreter,
        arguments: List<Any>
    ): Any {
        val text = arguments.map {
            interpreter.stringify(it)
        }.joinToString(" ")
        print(text)

        return getVal(interpreter, text)
    }
}