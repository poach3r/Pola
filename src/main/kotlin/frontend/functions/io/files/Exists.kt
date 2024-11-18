package org.poach3r.frontend.functions.io.files

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable
import java.io.File

class Exists(
    override val arity: Int = 1
) : PCallable {
    override fun call(interpreter: Interpreter, arguments: List<Any>): Any {
        return (arguments[0] as File).exists()
    }
}