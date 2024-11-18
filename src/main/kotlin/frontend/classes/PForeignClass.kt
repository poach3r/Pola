package org.poach3r.frontend.classes

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable
import org.poach3r.frontend.PInstance
import org.poach3r.frontend.functions.PFunction
import kotlin.String

data class PForeignClass(
    override val name: String,
    override val superclass: PForeignClass? = null,
    override val methods: HashMap<String, PCallable>,
    override val arity: Int = methods["init"]?.arity ?: 0
) : PClass {
    override fun call(
        interpreter: Interpreter,
        arguments: List<Any>
    ): Any {
        val instance = PInstance(this)
        (findMethod("init") as PFunction?)?.bind(instance)?.call(interpreter, arguments)

        return instance
    }

    override fun toString(fields: HashMap<String, Any>): String {
        return this.toString()
    }
}