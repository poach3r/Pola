package org.poach3r.frontend.functions.io.files

import org.poach3r.errors.RuntimeError
import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.functions.io.IOFunc
import java.io.File

class Read(
    override val arity: Int = 1
): IOFunc {
    override fun call(interpreter: Interpreter, arguments: List<Any>): Any {
        val file = (arguments[0] as File)

        if(!file.exists())
            throw RuntimeError(
                msg = "Cannot read file '${file.absolutePath}' as it does not exist."
            )

        if(!file.isFile)
            throw RuntimeError(
                msg = "Cannot read '${file.absolutePath}' as it is not a file."
            )

        if(!file.canRead())
            throw RuntimeError(
                msg = "Cannot read '${file.absolutePath}' due to a lack of permissions."
            )

        return getVal(interpreter, (arguments[0] as File).readLines().joinToString("\n"))
    }
}