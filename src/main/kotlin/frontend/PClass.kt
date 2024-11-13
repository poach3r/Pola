package org.poach3r.frontend

import org.poach3r.frontend.functions.PFunction

open class PClass(
    val name: String,
    val superclass: PClass? = null,
    val methods: HashMap<String, PFunction> = hashMapOf(),
    override val arity: Int = methods["init"]?.arity ?: 0
): PCallable {
    override fun call(
        interpreter: Interpreter,
        arguments: List<Any>
    ): Any {
        val instance = PInstance(this)
        findMethod("init")?.bind(instance)?.call(interpreter, arguments)

        return instance
    }

    fun findMethod(name: String): PFunction? {
        methods[name]?.let {
            return it
        }

        superclass?.let {
            return it.findMethod(name)
        }

        return null
    }

    override fun toString(): String {
        return name
    }
}

fun arity(methods: HashMap<String, PFunction>): Int {
    methods.get("init")?.let {
        return it.arity
    }

    return 0
}