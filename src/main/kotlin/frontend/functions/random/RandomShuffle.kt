package org.poach3r.frontend.functions.random

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.classes.RandomFunc
import org.poach3r.frontend.functions.arrays.ArrayFunc

class RandomShuffle : RandomFunc(), ArrayFunc {
    override val arity: Int = 1

    override fun call(interpreter: Interpreter, arguments: List<Any>): Any {
        val list = getList(arguments[0])
        return list.shuffled()
    }
}
