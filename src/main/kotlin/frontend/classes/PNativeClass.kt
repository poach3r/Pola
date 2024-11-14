package org.poach3r.frontend.classes

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PInstance

interface PNativeClass : PClass {
    override fun call(
        interpreter: Interpreter,
        arguments: List<Any>
    ): Any {
        return PInstance(
            clazz = this,
            fields = if (arguments.isNotEmpty())
                hashMapOf("__literalValue" to arguments)
            else
                hashMapOf(),
        )
    }
}