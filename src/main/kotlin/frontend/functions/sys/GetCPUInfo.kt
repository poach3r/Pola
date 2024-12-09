package org.poach3r.frontend.functions.sys

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable
import java.io.File
import java.lang.management.ManagementFactory
import java.text.DecimalFormat

class GetCPUInfo : PCallable {
    override val arity: Int = 0

    override fun call(interpreter: Interpreter, arguments: List<Any>): Any {
        val osBean = ManagementFactory.getOperatingSystemMXBean()
        val availableProcessors = osBean.availableProcessors
        val systemLoadAverage = osBean.systemLoadAverage

        return CPUInfo.createInstance(availableProcessors, systemLoadAverage)
    }
}