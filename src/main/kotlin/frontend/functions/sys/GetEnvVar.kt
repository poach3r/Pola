package org.poach3r.frontend.functions.sys

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable

class GetEnvVar : PCallable {
    override val arity: Int = 1

    override fun call(interpreter: Interpreter, arguments: List<Any>): Any {
        val varName = arguments[0].toString()
        return System.getenv(varName) ?: ""
    }
}