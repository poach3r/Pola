package org.poach3r.frontend.functions.io

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable

class Println(
    override val arity: Int = -1
) : PCallable {
    override fun call(
        interpreter: Interpreter,
        arguments: List<Any>
    ): Any {
        val text = arguments.map {
            interpreter.stringify(it)
        }.joinToString(" ")
        println(text)

        return 0
        //return return (interpreter.globals.variables.get("Standard")!!.value as StandardLibrary).methods.get("array")!!.call(interpreter, list)
    }
}