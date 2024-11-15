package frontend.functions.standardLibrary

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable

class Print(
    override val arity: Int = -1
) : PCallable {
    override fun call(
        interpreter: Interpreter,
        arguments: List<Any>
    ): Any {
        val text = arguments.map {
            interpreter.stringify(it)
        }.joinToString(" ")
        print(text)

        return interpreter.createString(text)
    }
}