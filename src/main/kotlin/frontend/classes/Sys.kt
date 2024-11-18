package org.poach3r.frontend.classes

import frontend.functions.sys.Exit
import org.poach3r.frontend.PCallable
import org.poach3r.frontend.functions.sys.GetEnvVar
import kotlin.String

class Sys(
    override val name: String = "Sys",
    override val arity: Int = 0,
    override val methods: HashMap<String, PCallable> = hashMapOf(
        "exit" to Exit(),
        "getEnvVar" to GetEnvVar(),
    ),
    override val superclass: PClass? = null,
) : PNativeClass