package org.poach3r.frontend.functions.arrays

import org.poach3r.frontend.Interpreter

class Map(
    override val arity: Int = 2
) : ArrayFunc {
    override fun call(
        interpreter: Interpreter,
        arguments: List<Any>
    ): Any {
        val list = getList(arguments[0])
        val function = getFunc(arguments, 1)
        val result = if (function.arity == 1) {
            list.map {
                function.call(interpreter, listOf(it))
            }
        } else {
            list.mapIndexed { index, argument ->
                function.call(interpreter, listOf(argument, index.toDouble()))
            }
        }

        return getResult(result, arguments[0], interpreter)
    }
}