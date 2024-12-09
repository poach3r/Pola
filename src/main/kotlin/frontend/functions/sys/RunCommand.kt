package org.poach3r.frontend.functions.sys

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable
import java.io.BufferedReader
import java.io.InputStreamReader

class RunCommand : PCallable {
    override val arity: Int = 1

    override fun call(interpreter: Interpreter, arguments: List<Any>): Any {
        val command = arguments[0].toString()
        return try {
            val process = Runtime.getRuntime().exec(command)
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val output = reader.readText()
            reader.close()
            process.waitFor()
            output.trim()
        } catch (e: Exception) {
            throw RuntimeException("Error executing command: ${e.message}")
        }
    }
}