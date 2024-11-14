package frontend.functions.standardLibrary.arrayLibrary

import org.poach3r.errors.RuntimeError
import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.functions.standardLibrary.arrayLibrary.ArrayFunc

class Filter(
    override val arity: Int = 2
): ArrayFunc {
    override fun call(
        interpreter: Interpreter,
        arguments: List<Any>
    ): Any {
        val list = getList(arguments[0])
        val function = getFunc(list, arguments[1])
        val result = list.filter {
            val result = function.call(interpreter, listOf(it))
            if(result !is Boolean)
                throw RuntimeError(
                    msg = "Attempted to perform a filter operation with a function that does not return a boolean."
                )

            result
        }

        return getResult(result, arguments[0], interpreter)
    }
}