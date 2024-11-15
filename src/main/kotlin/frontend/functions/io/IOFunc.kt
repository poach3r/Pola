package org.poach3r.frontend.functions.io

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable
import org.poach3r.frontend.classes.Strings

interface IOFunc: PCallable {
    fun getVal(interpreter: Interpreter, text: String): Any {
        interpreter.globals.variables.get("strings")?.let {
            return (it.value as Strings).call(interpreter, listOf(text))
        }

        return text
    }
}