package org.poach3r

import org.poach3r.backend.Parser
import org.poach3r.backend.Scanner
import org.poach3r.errors.ArgError
import org.poach3r.errors.PError
import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.Resolver

fun main(args: Array<String>) {
    try {
        Main(Config.of(args)).main()
    } catch(e: ArgError) {
        System.err.println(e.message)
    } catch(e: Error) {
        e.printStackTrace()
    }
}

class Main(val config: Config, ) {
    val scanner = Scanner()
    val parser = Parser()
    val interpreter = Interpreter()
    val resolver = Resolver(interpreter)

    fun main() {
        if(config.file == null)
            shell()
        else
            lang(config.file.readLines().joinToString("\n"))
    }

    private fun lang(source: String) {
        interpret(source)
    }

    private fun shell() {
        while(true) {
            print("$ ")
            interpret(readln())
        }
    }

    private fun interpret(source: String) {
        try {
            val statements = parser.parse(scanner.scanTokens(source))
            resolver.resolve(statements)
            interpreter.interpret(statements)
        } catch(e: PError) {
            System.err.println(e.message)

            if(config.printStackTrace)
                e.printStackTrace()
        } catch(e: Error) {
            e.printStackTrace()
            println() // HACK without these printlns the prompt is on the line with the error message, dunno why
        }
    }
}

