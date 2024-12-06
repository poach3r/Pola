package org.poach3r.frontend.functions.sys

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable

class SetEnvVar : PCallable {
    override val arity: Int = 2

    override fun call(interpreter: Interpreter, arguments: List<Any>): Any {
        val varName = arguments[0].toString()
        val varValue = arguments[1].toString()

        return try {
            // Note: Directly setting environment variables is not straightforward in JVM from what I've found
            // This is a workaround and may not work in all environments
            val env = System.getenv()
            val fieldSysenv = env.javaClass.getDeclaredField("m")
            fieldSysenv.isAccessible = true
            val writableEnv = fieldSysenv.get(env) as MutableMap<String, String>
            writableEnv[varName] = varValue
            true
        } catch (e: Exception) {
            false
        }
    }
}