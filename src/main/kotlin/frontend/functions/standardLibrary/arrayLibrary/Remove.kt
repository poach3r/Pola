package org.poach3r.frontend.functions.standardLibrary.arrayLibrary

import org.poach3r.frontend.Interpreter

class Remove(
    override val arity: Int = 2,
): ArrayFunc {
    override fun call(
        interpreter: Interpreter,
        arguments: List<Any>
    ): Any {
        val list = getList(arguments[0])
        val index = getIndex(list, arguments[1])

        list.removeAt(index)

        return getResult(list, arguments[0], interpreter)
    }
}