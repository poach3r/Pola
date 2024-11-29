package org.poach3r.frontend.functions

import org.poach3r.Main
import org.poach3r.errors.RuntimeError
import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable
import org.poach3r.frontend.classes.Array
import org.poach3r.frontend.classes.Errors
import org.poach3r.frontend.classes.IO
import org.poach3r.frontend.classes.Math
import org.poach3r.frontend.classes.String
import org.poach3r.frontend.classes.Sys
import org.poach3r.frontend.classes.gui.Gui
import java.io.File

class Import(
    override val arity: Int = 1
) : PCallable {
    override fun call(
        interpreter: Interpreter,
        arguments: List<Any>
    ): Any {
        return when (arguments[0].toString()) {
            // check for the standard libraries
            "pola/array" -> interpreter.globals.define("array", false, Array())
            "pola/io" -> interpreter.globals.define("io", false, IO().call(interpreter, listOf()))
            "pola/sys" -> interpreter.globals.define("sys", false, Sys().call(interpreter, listOf()))
            "pola/errors" -> interpreter.globals.define("errors", false, Errors().call(interpreter, listOf()))
            "pola/gui" -> interpreter.globals.define("gui", false, Gui().call(interpreter, listOf()))
            "pola/math" -> interpreter.globals.define("math", false, Math().call(interpreter, listOf()))

            // if the library isn't native then check for it via file path
            else -> {
                val possibleClass = File((arguments[0] as kotlin.String) + ".pola")

                if (!possibleClass.exists()) {
                    throw RuntimeError(
                        msg = "Cannot find class '${arguments[0]}'..",
                    )
                }

                if (!possibleClass.isFile) {
                    throw RuntimeError(
                        msg = "Cannot import '${arguments[0]}' as it is not a file.",
                    )
                }

                if (!possibleClass.canRead()) {
                    throw RuntimeError(
                        msg = "Cannot read class '${arguments[0]}'.",
                    )
                }

                Main.interpret(possibleClass)

                arguments[0] as kotlin.String // this is temporary
            }
        }
    }
}