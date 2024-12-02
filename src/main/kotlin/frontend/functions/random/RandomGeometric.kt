package org.poach3r.frontend.functions.random

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.classes.RandomFunc
import kotlin.random.Random
import kotlin.math.ln

class RandomGeometric : RandomFunc() {
    override val arity: Int = 1 // the probability of success (p).

    override fun call(interpreter: Interpreter, arguments: List<Any>): Any {
        val p = (arguments[0] as? Double)
            ?: throw IllegalArgumentException("Argument must be a number (Double).")

        if (p <= 0 || p > 1) throw IllegalArgumentException("Probability must be in the range (0, 1].")

        return generateGeometric(p)
    }

    private fun generateGeometric(p: Double): Int {
        return (ln(1 - Random.nextDouble()) / ln(1 - p)).toInt() + 1
    }
}
