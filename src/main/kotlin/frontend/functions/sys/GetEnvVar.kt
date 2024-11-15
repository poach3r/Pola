package org.poach3r.frontend.functions.sys

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable

class GetEnvVar(
    override val arity: Int = 1
): PCallable {
    override fun call(interpreter: Interpreter, arguments: List<Any>): Any {
        return interpreter.createString(System.getenv(arguments[0].toString()))
    }
}