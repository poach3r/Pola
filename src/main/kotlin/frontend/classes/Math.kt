package org.poach3r.frontend.classes

import frontend.functions.math.Pow
import org.poach3r.frontend.PCallable
import org.poach3r.frontend.functions.math.Cbrt
import org.poach3r.frontend.functions.math.Sqrt
import kotlin.String

class Math(
    override val name: String = "math",
    override val arity: Int = 0,
    override val methods: HashMap<String, PCallable> = hashMapOf(
        "pow" to Pow(),
        "sqrt" to Sqrt(),
        "cbrt" to Cbrt()
    ),
    override val superclass: PClass? = null
): PNativeClass