package org.poach3r.frontend.classes

import frontend.functions.errors.Throw
import frontend.functions.errors.Try
import org.poach3r.frontend.PCallable
import kotlin.String

class Errors(
    override val name: String = "Errors",
    override val arity: Int = 0,
    override val methods: HashMap<String, PCallable> = hashMapOf(
        "throw" to Throw(),
        "try" to Try()
    ),
    override val superclass: PClass? = null,
) : PNativeClass