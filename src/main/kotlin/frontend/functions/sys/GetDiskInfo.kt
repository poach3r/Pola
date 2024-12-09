package org.poach3r.frontend.functions.sys

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable
import java.io.File
import java.lang.management.ManagementFactory
import java.text.DecimalFormat


class GetDiskInfo : PCallable {
    override val arity: Int = 0

    override fun call(interpreter: Interpreter, arguments: List<Any>): Any {
        val root = File("/")
        return DiskInfo.createInstance(
            root.totalSpace / (1024 * 1024 * 1024.0),
            root.freeSpace / (1024 * 1024 * 1024.0),
            root.usableSpace / (1024 * 1024 * 1024.0)
        )
    }
}