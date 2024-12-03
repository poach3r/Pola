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
    Global.main = Main(args)
    Global.main.interpret()
}

object Global {
    lateinit var main: Main
}

class Main(
    args: Array<String>
) {
    private val scanner = Scanner()
    private val parser = Parser()
    private val interpreter = Interpreter()
    private val resolver = Resolver(interpreter)
    private var config: Config? = null


    init {
        try {
            config = Config.of(args)
        } catch (e: ArgError) {
            System.err.println(e.message)
            exitProcess(1)
        }
    }

    fun interpret(
        file: File = config!!.file!!
    ) {
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
