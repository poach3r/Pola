package org.poach3r.frontend.functions

import org.poach3r.frontend.Environment
import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable
import org.poach3r.Stmt
import org.poach3r.errors.BreakError
import org.poach3r.errors.ReturnError
import org.poach3r.frontend.PInstance

class PFunction(
    private val declaration: Stmt.Companion.Function,
    private val closure: Environment,
    private val isInitializer: Boolean
): PCallable {
    override val arity: Int = declaration.parameters.size

    fun bind(instance: PInstance): PFunction {
        return PFunction(
            declaration = declaration,
            isInitializer = isInitializer,
            closure = Environment(closure).apply {
                this.define(
                    name = "this",
                    value = instance,
                    mutable = false
                )
            }
        )
    }

    override fun call(interpreter: Interpreter, arguments: List<Any>): Any {
        val environment = Environment(closure)

        for(i in 0..declaration.parameters.size - 1) {
            environment.define(
                name = declaration.parameters[i].lexeme,
                mutable = false,
                value = arguments[i]
            )
        }

        try {
            interpreter.executeBlock(
                statements = declaration.body,
                environment = environment
            )
        } catch(returnValue: ReturnError) {
            if(isInitializer)
                return closure.getAt(0, "this")!!

            return returnValue.value
        }

        if(isInitializer)
            return closure.getAt(0, "this")!!

        return 0
    }
}