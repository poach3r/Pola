package org.poach3r.frontend.functions.standardLibrary.arrayLibrary

import org.poach3r.errors.RuntimeError
import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable
import org.poach3r.frontend.classes.StandardLibrary
import org.poach3r.frontend.functions.PFunction

class Get(
    override val arity: Int = 2
): ArrayFunc {
    override fun call(
        interpreter: Interpreter,
        arguments: List<Any>
    ): Any {
        val list = getList(arguments[0])
        return list[getIndex(list, arguments[1])]
    }
}