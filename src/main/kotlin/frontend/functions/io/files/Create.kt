package org.poach3r.frontend.functions.io.files

import org.poach3r.errors.RuntimeError
import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable
import java.io.File

class Create(
    override val arity: Int = 1
) : PCallable {
    override fun call(interpreter: Interpreter, arguments: List<Any>): Any {
        val file = (arguments[0] as File)

        if (file.exists())
            throw RuntimeError(
                msg = "Cannot create file '${file.absolutePath}' as it already exists."
            )

        return file.createNewFile()
    }
}