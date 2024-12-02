package org.poach3r.frontend.functions.random

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.classes.RandomFunc
import kotlin.random.Random
import kotlin.math.exp
import kotlin.math.ln

class RandomPoisson : RandomFunc() {
    override val arity: Int = 1 // Expects the mean (lambda).

    override fun call(interpreter: Interpreter, arguments: List<Any>): Any {
        val lambda = (arguments[0] as? Double)
            ?: throw IllegalArgumentException("Argument must be a number (Double).")

        if (lambda <= 0) throw IllegalArgumentException("Lambda must be positive.")

        return generatePoisson(lambda)
    }

    private fun generatePoisson(lambda: Double): Int {
        val l = exp(-lambda)
        var k = 0
        var p = 1.0

        do {
            k++
            p *= Random.nextDouble()
        } while (p > l)

        return k - 1
    }
}
