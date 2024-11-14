package org.poach3r.frontend.classes

import frontend.functions.standardLibrary.arrayLibrary.Filter
import org.poach3r.errors.RuntimeError
import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable
import org.poach3r.frontend.PInstance
import org.poach3r.frontend.functions.standardLibrary.arrayLibrary.ForEach
import org.poach3r.frontend.functions.standardLibrary.arrayLibrary.Get
import org.poach3r.frontend.functions.standardLibrary.arrayLibrary.Map
import org.poach3r.frontend.functions.standardLibrary.arrayLibrary.Remove
import org.poach3r.frontend.functions.standardLibrary.arrayLibrary.Replace
import org.poach3r.frontend.functions.standardLibrary.arrayLibrary.Set

class Strings(
    override val name: String = "Strings",
    override val arity: Int = 1,
    override val methods: HashMap<String, PCallable> = hashMapOf(
        "get" to Get(),
        "set" to Set(),
        "remove" to Remove(),
        "filter" to Filter(),
        "foreach" to ForEach(),
        "map" to Map(),
        "replace" to Replace()
    ),
    override val superclass: PClass? = null,
): PNativeClass{
    override fun toString(fields: HashMap<String, Any>): String {
        return fields.get("__literalValue").toString()
    }

    override fun call(
        interpreter: Interpreter,
        arguments: List<Any>
    ): Any {
        return PInstance(
            clazz = this,
            fields = if(arguments.isNotEmpty())
                hashMapOf("__literalValue" to arguments[0].toString())
            else
                hashMapOf(),
        )
    }
}

