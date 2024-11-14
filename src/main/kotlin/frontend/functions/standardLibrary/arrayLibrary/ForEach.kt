package org.poach3r.frontend.functions.standardLibrary.arrayLibrary

import org.poach3r.frontend.Interpreter

class ForEach(
    override val arity: Int = 2
) : ArrayFunc {
    override fun call(
        interpreter: Interpreter,
        arguments: List<Any>
    ): Any {
        val list = getList(arguments[0])
        val function = getFunc(list, arguments[1])

        if (function.arity == 1) {
            list.forEach {
                function.call(interpreter, listOf(it))
            }
        } else {
            list.forEachIndexed { index, argument ->
                function.call(interpreter, listOf(argument, index))
            }
        }

        return 0
    }
}
