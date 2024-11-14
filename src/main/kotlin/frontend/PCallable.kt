package org.poach3r.frontend

interface PCallable {
    val arity: Int

    fun call(
        interpreter: Interpreter,
        arguments: List<Any> = listOf()
    ): Any
}