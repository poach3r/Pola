package org.poach3r.frontend.classes

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable
import org.poach3r.frontend.functions.PFunction
import kotlin.String

interface PClass : PCallable {
    val name: kotlin.String
    val superclass: PClass?
    val methods: HashMap<kotlin.String, PCallable>
    override val arity: Int

    override fun call(interpreter: Interpreter, arguments: List<Any>): Any

    fun findMethod(name: kotlin.String): PCallable? {
        methods[name]?.let {
            return it
        }

        superclass?.let {
            return it.findMethod(name)
        }

        return null
    }

    fun arity(methods: HashMap<kotlin.String, PFunction>): Int {
        methods.get("init")?.let {
            return it.arity
        }

        return 0
    }

    fun toString(fields: HashMap<kotlin.String, Any>): String {
        return this.toString()
    }
}