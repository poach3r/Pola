package org.poach3r.frontend.functions.sys

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable
import java.io.File
import java.lang.management.ManagementFactory
import java.text.DecimalFormat

class GetMemoryInfo : PCallable {
    override val arity: Int = 0

    override fun call(interpreter: Interpreter, arguments: List<Any>): Any {
        val runtime = Runtime.getRuntime()
        val maxMemory = runtime.maxMemory()
        val allocatedMemory = runtime.totalMemory()
        val freeMemory = runtime.freeMemory()

        return MemoryInfo().let { memoryInfo ->
            MemoryInfo.createInstance(
                memoryInfo.bytesToMB(maxMemory),
                memoryInfo.bytesToMB(allocatedMemory),
                memoryInfo.bytesToMB(freeMemory),
                memoryInfo.bytesToMB(allocatedMemory - freeMemory)
            )
        }
    }
}