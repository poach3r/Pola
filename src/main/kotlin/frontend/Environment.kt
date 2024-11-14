package org.poach3r.frontend

import org.poach3r.Token
import org.poach3r.errors.RuntimeError

class Environment(
    val enclosing: Environment? = null
) {
    val variables = HashMap<String, Variable>()

    fun define(
        token: Token,
        mutable: Boolean,
        value: Any
    ): Any {
        if (variables.contains(token.lexeme))
            throw RuntimeError(
                line = token.line,
                msg = "Variable '${token.lexeme}' already exists."
            )

        variables.put(token.lexeme, Variable(mutable, value))

        return value
    }

    fun define(
        name: String,
        mutable: Boolean,
        value: Any
    ): Any {
        if (variables.contains(name))
            throw RuntimeError(
                msg = "Variable '$name' already exists.",
            )

        variables.put(name, Variable(mutable, value))

        return value
    }

    fun get(token: Token): Any {
        variables.get(token.lexeme)?.let {
            return it.value
        }

        if (enclosing != null)
            return enclosing.get(token)

        throw RuntimeError(
            line = token.line,
            msg = "Undefined variable '${token.lexeme}'."
        )
    }

    fun assign(token: Token, value: Any) {
        variables.get(token.lexeme)?.let {
            if (it.mutable) {
                it.value = value
                return
            }

            throw RuntimeError(
                line = token.line,
                msg = "Cannot redefine immutable variable '${token.lexeme}'."
            )
        }

        if (enclosing != null) {
            enclosing.assign(token, value)
            return
        }

        throw RuntimeError(
            line = token.line,
            msg = "Undefined variable '${token.lexeme}'."
        )
    }

    fun getAt(distance: Int, name: String): Any? {
        return ancestor(distance).variables[name]?.value
    }

    fun ancestor(distance: Int): Environment {
        var environment = this

        for (i in 0..distance - 1) {
            environment = environment.enclosing!!
        }

        return environment
    }

    fun assignAt(distance: Int, name: Token, value: Any, mutable: Boolean) {
        ancestor(distance).variables.put(
            name.lexeme,
            Variable(
                mutable = mutable,
                value = value
            )
        )
    }

    data class Variable(
        val mutable: Boolean,
        var value: Any
    )
}