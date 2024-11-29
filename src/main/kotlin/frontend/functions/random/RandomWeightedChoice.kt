package org.poach3r.frontend.functions.random

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.classes.RandomFunc

class RandomWeightedChoice : RandomFunc() {
    override val arity: Int = 1

    override fun call(
        interpreter: Interpreter,
        arguments: List<Any>
    ): Any {
        val weightedList = arguments[0] as List<Pair<Any, Double>>

        val totalWeight = weightedList.sumOf { it.second }
        val randomValue = getRandomInstance().nextDouble(totalWeight)

        var accumulatedWeight = 0.0
        for ((item, weight) in weightedList) {
            accumulatedWeight += weight
            if (randomValue <= accumulatedWeight) {
                return item
            }
        }

        // Fallback to last item if something goes wrong
        return weightedList.last().first
    }
}