package org.poach3r.frontend.classes

import frontend.functions.standardLibrary.arrayLibrary.Filter
import org.poach3r.frontend.PCallable
import org.poach3r.frontend.functions.arrays.ForEach
import org.poach3r.frontend.functions.arrays.Get
import org.poach3r.frontend.functions.arrays.Map
import org.poach3r.frontend.functions.arrays.Remove
import org.poach3r.frontend.functions.arrays.Set

class Arrays(
    override val name: String = "Arrays",
    override val arity: Int = -1,
    override val methods: HashMap<String, PCallable> = hashMapOf(
        "get" to Get(),
        "set" to Set(),
        "remove" to Remove(),
        "filter" to Filter(),
        "foreach" to ForEach(),
        "map" to Map(),
    ),
    override val superclass: PClass? = null,
) : PNativeClass {
    override fun toString(fields: HashMap<String, Any>): String {
        return fields.get("__literalValue").toString()
    }
}