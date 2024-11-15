package org.poach3r.frontend.functions.io.files

import org.poach3r.errors.RuntimeError
import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable
import java.io.File

class Write(
    override val arity: Int = 2
): PCallable {
    override fun call(interpreter: Interpreter, arguments: List<Any>): Any {
        val file = arguments[0] as File
        val text = arguments[1].toString()

        if(!file.exists())
            throw RuntimeError(
                msg = "Cannot write to file '${file.absolutePath}' as it does not exist."
            )

        if(!file.isFile)
            throw RuntimeError(
                msg = "Cannot write to '${file.absolutePath}' as it is not a file."
            )

        if(!file.canWrite())
            throw RuntimeError(
                msg = "Cannot write to '${file.absolutePath}' due to a lack of permissions."
            )

        file.writeText(text)
        return true
    }
}