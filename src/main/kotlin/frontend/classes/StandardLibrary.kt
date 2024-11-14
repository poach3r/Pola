package org.poach3r.frontend.classes

import frontend.functions.standardLibrary.Exit
import frontend.functions.standardLibrary.Print
import org.poach3r.frontend.PCallable
import org.poach3r.frontend.functions.standardLibrary.Println

data class StandardLibrary(
    override val name: String = "Standard",
    override val methods: HashMap<String, PCallable> = hashMapOf(
        // functions
        "exit" to Exit(),
        "print" to Print(),
        "println" to Println(),

        // classes
        "array" to Arrays(),
        "string" to Strings()
    ),
    override val arity: Int = 0,
    override val superclass: PClass? = null,
): PNativeClass{
    override fun toString(fields: HashMap<String, Any>): String {
        return this.toString()
    }
}



