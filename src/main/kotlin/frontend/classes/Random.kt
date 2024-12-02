package org.poach3r.frontend.classes

import frontend.functions.random.RandomSeed
import org.poach3r.frontend.PCallable
import org.poach3r.frontend.PInstance
import org.poach3r.frontend.functions.random.*;
import kotlin.random.Random
import kotlin.String

class Random(
    override val name: String = "Random",
    override val arity: Int = -1,
    override val methods: HashMap<String, PCallable> = hashMapOf(
        "int" to RandomInt(),
        "float" to RandomFloat(),
        "choice" to RandomChoice(),
        "seed" to RandomSeed(),
        "sample" to RandomSample(),
        "bool" to RandomBool(),
        "normal" to RandomNormal(),
        "uniform" to RandomUniform(),
        "triangular" to RandomTriangular(),
        "weighted_choice" to RandomWeightedChoice(),
        "gaussian" to RandomGaussian(),
        "geometric" to RandomGeometric(),
        "shuffle" to RandomShuffle(),
        "zipf" to RandomZipf(),
        "uuid" to RandomUUID(),
        "poisson" to RandomPoisson(),
    ),
    override val superclass: PClass? = null
) : PNativeClass {
    override fun toString(fields: HashMap<String, Any>): String {
        return "Random Library"
    }

    companion object {
        private var random: Random = Random.Default
    }
}

// Base random function utility
abstract class RandomFunc : PCallable {
    protected fun getRandomInstance(): Random = Random
}