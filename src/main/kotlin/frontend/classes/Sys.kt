package org.poach3r.frontend.classes

import frontend.functions.standardLibrary.Exit
import org.poach3r.frontend.PCallable

class Sys(
    override val name: String = "Sys",
    override val arity: Int = 0,
    override val methods: HashMap<String, PCallable> = hashMapOf(
        "exit" to Exit()
    ),
    override val superclass: PClass? = null,
): PNativeClass