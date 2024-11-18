package org.poach3r

import org.poach3r.backend.Parser
import org.poach3r.backend.Scanner
import org.poach3r.errors.ArgError
import org.poach3r.errors.PError
import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.Resolver
import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    Main.main(args)
}

object Main {
    val scanner = Scanner()
    val parser = Parser()
    val interpreter = Interpreter()
    val resolver = Resolver(interpreter)
    var config: Config? = null

    fun main(args: Array<String>) {
        try {
            config = Config.of(args)
        } catch (e: ArgError) {
            System.err.println(e.message)
            exitProcess(1)
        }

        interpret(config!!.file!!)
    }

    fun interpret(file: File) {
        try {
            val statements = parser.parse(scanner.scanTokens(file.readLines().joinToString("\n")))
            resolver.resolve(statements)
            interpreter.interpret(statements)
        } catch (e: PError) {
            if (config!!.printStackTrace)
                e.printStackTrace()
            else
                System.err.println(e.message)

            exitProcess(1)
        } catch (e: Error) {
            e.printStackTrace()
            exitProcess(1)
        }
    }
}