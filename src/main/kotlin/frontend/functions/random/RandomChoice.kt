package org.poach3r.frontend.functions.random

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PInstance
import org.poach3r.frontend.classes.RandomFunc
import org.poach3r.frontend.functions.arrays.ArrayFunc
import kotlin.random.Random

class RandomChoice : RandomFunc(), ArrayFunc {
    override val arity: Int = 1

    private fun extractList(arg: Any): MutableList<Any> {
        return when (arg) {
            is PInstance -> arg.fields["__literalValue"] as MutableList<Any>
            is MutableList<*> -> arg as MutableList<Any>
            else -> throw RuntimeException("Expected an array or list")
        }
    }

    override fun call(interpreter: org.poach3r.frontend.Interpreter, arguments: List<Any>): Any {
        val list = extractList(arguments[0])
        return list[Random.nextInt(list.size)]
    }

}