package org.poach3r.frontend

import org.poach3r.Token
import org.poach3r.errors.RuntimeError

class PInstance(
    private val clazz: PClass
) {
    private val fields = HashMap<String, Any>()

    fun get(name: Token): Any {
        fields.get(name.lexeme)?.let {
            return it
        }

        clazz.findMethod(name.lexeme)?.let {
            return it.bind(this)
        }

        throw RuntimeError(name.line, "Undefined property '${name.lexeme}'.")
    }

    fun set(name: Token, value: Any) {
        fields.put(name.lexeme, value)
    }

    override fun toString(): String {
        return "${clazz.name} $fields"
    }
}