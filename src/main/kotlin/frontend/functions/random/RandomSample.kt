package org.poach3r.frontend.functions.random

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.classes.RandomFunc
import org.poach3r.frontend.functions.arrays.ArrayFunc
import kotlin.random.Random

class RandomSample : RandomFunc(), ArrayFunc {
    override val arity: Int = 2 // Expects two arguments: the list and the sample size.

    override fun call(interpreter: Interpreter, arguments: List<Any>): Any {
        val list = getList(arguments[0])
        val sampleSize = getSampleSize(arguments[1], list.size)

        return list.shuffled(Random).take(sampleSize)
    }

    private fun getSampleSize(arg: Any, listSize: Int): Int {
        if (arg is Double) {
            val sampleSize = arg.toInt()
            if (sampleSize in 0..listSize) {
                return sampleSize
            } else {
                throw IllegalArgumentException("Sample size must be between 0 and the size of the list.")
            }
        } else {
            throw IllegalArgumentException("Second argument must be a number (Double).")
        }
    }
}
