package org.poach3r.frontend.functions.io

import org.poach3r.frontend.Interpreter

class Println(
    override val arity: Int = -1
) : IOFunc {
    override fun call(
        interpreter: Interpreter,
        arguments: List<Any>
    ): Any {
        val text = arguments.map {
            interpreter.stringify(it)
        }.joinToString(" ")
        println(text)

        return getVal(interpreter, text)
    }
}