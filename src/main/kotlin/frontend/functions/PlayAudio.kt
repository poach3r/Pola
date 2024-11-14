package org.poach3r.frontend.functions

import org.poach3r.errors.RuntimeError
import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable
import java.io.File

class PlayAudio(
    override val arity: Int = 1
) : PCallable {
    override fun call(
        interpreter: Interpreter,
        arguments: List<Any>
    ): Any {
        val file = File(interpreter.stringify(arguments[0]))
        if (!file.exists() || !file.isFile) {
            throw RuntimeError(
                msg = "File '${file.absolutePath}' does not exist or is not a file.'"
            )
        }

        TODO()
    }
}