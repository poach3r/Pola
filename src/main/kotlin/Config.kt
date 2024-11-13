package org.poach3r

import org.poach3r.errors.ArgError
import java.io.File

data class Config(
    val printStackTrace: Boolean = false,
    val file: File? = null,
) {
    companion object {
        fun of(args: Array<String>): Config {
            var printStackTrace = false
            var file: File? = null
            args.forEachIndexed { index, arg ->
                when(arg) {
                    "-v" -> printStackTrace = true
                    "-f" -> {
                        with(File(consume(args, index))) {
                            if(!this.isFile)
                                throw Error("File ${args[index + 1]} does not exist.")

                            file = this
                        }
                    }
                }
            }

            return Config(printStackTrace, file)
        }

        private fun consume(args: Array<String>, index: Int): String {
            if(args.size == index + 1)
                throw ArgError(index, "Argument ${args[index]} requires option which was not found.")

            return args[index + 1]
        }
    }
}