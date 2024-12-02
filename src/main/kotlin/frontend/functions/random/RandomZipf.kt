package org.poach3r.frontend.functions.random

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.classes.RandomFunc
import kotlin.math.pow
import kotlin.random.Random

class RandomZipf : RandomFunc() {
    override val arity: Int = 2 // the size (n) and the exponent (s).

    override fun call(interpreter: Interpreter, arguments: List<Any>): Any {
        val n = (arguments[0] as? Double)?.toInt()
            ?: throw IllegalArgumentException("First argument must be a positive integer (size).")

        val s = (arguments[1] as? Double)
            ?: throw IllegalArgumentException("Second argument must be a number (exponent).")

        if (n <= 0 || s <= 0) throw IllegalArgumentException("Size and exponent must be positive.")

        return generateZipf(n, s)
    }

    private fun generateZipf(n: Int, s: Double): Int {
        val harmonicSum = (1..n).sumOf { i -> 1.0 / i.toDouble().pow(s) }
        val normalizedCdf = (1..n).scan(0.0) { acc, i -> acc + 1.0 / i.toDouble().pow(s) / harmonicSum }
        val randomValue = Random.nextDouble()

        return normalizedCdf.indexOfFirst { it >= randomValue } - 1
    }
}
