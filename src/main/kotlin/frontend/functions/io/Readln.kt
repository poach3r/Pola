package org.poach3r.frontend.functions.io

import jdk.internal.agent.Agent.getText
import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable
import org.poach3r.frontend.classes.Strings

class Readln(
    override val arity: Int = 0
): IOFunc {
    override fun call(
        interpreter: Interpreter,
        arguments: List<Any>
    ): Any {
        return getVal(interpreter, readln())
    }
}