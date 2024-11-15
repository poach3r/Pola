package org.poach3r.frontend.functions.arrays

import org.poach3r.frontend.Interpreter

class Get(
    override val arity: Int = 2
) : ArrayFunc {
    override fun call(
        interpreter: Interpreter,
        arguments: List<Any>
    ): Any {
        val list = getList(arguments[0])
        return list[getIndex(list, arguments[1])]
    }
}