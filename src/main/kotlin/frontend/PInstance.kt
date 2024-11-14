package org.poach3r.frontend

import org.poach3r.Token
import org.poach3r.errors.RuntimeError
import org.poach3r.frontend.classes.PClass
import org.poach3r.frontend.functions.PFunction

class PInstance(
    val clazz: PClass,
    private val fields: HashMap<String, Any> = hashMapOf()
) {
    fun get(name: Token): Any {
        fields.get(name.lexeme)?.let {
            return it
        }

        clazz.findMethod(name.lexeme)?.let {
            if (it is PFunction)
                return it.bind(this)

            return it
        }

        throw RuntimeError(name.line, "Undefined property '${name.lexeme}'.")
    }

    fun get(name: String): Any {
        fields.get(name)?.let {
            return it
        }

        clazz.findMethod(name)?.let {
            if (it is PFunction)
                return it.bind(this)

            return it
        }

        throw RuntimeError(
            msg = "Undefined property '${name}'."
        )
    }

    fun set(name: Token, value: Any) {
        fields.put(name.lexeme, value)
    }

    override fun toString(): String {
        return clazz.toString(fields)
    }
}