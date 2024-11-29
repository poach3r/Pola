package org.poach3r.frontend.functions.random

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PInstance
import org.poach3r.frontend.classes.RandomFunc

class RandomSample : RandomFunc() {
    override val arity: Int = 2

    private fun extractList(arg: Any): MutableList<Any> {
        return when (arg) {
            is PInstance -> arg.fields["__literalValue"] as MutableList<Any>
            is MutableList<*> -> arg as MutableList<Any>
            else -> throw RuntimeException("Expected an array or list")
        }
    }

    override fun call(
        interpreter: Interpreter,
        arguments: List<Any>
    ): Any {
        val list = extractList(arguments[0])
        val k = (arguments[1] as Double).toInt()

        if (k > list.size)
            throw IllegalArgumentException("Sample size cannot be larger than list size")

        return list.shuffled().take(k)
    }
}