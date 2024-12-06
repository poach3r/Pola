package org.poach3r.frontend.classes

import org.poach3r.frontend.PCallable
import org.poach3r.frontend.functions.sys.*
import kotlin.String

class Sys(
    override val name: String = "Sys",
    override val arity: Int = 0,
    override val methods: HashMap<String, PCallable> = hashMapOf(
        "exit" to Exit(),
        "runCommand" to RunCommand(),
        "getSocket" to GetSocket(),
        "getEnv" to GetEnvVar(), // Renamed
        "setEnv" to SetEnvVar(),
        "getCPUInfo" to GetCPUInfo(),
        "getMemoryInfo" to GetMemoryInfo(),
        "getDiskInfo" to GetDiskInfo(),
    ),
    override val superclass: PClass? = null,
) : PNativeClass