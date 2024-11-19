package org.poach3r.frontend.classes.gui

import org.poach3r.frontend.PCallable
import org.poach3r.frontend.classes.PClass
import org.poach3r.frontend.classes.PNativeClass
import kotlin.String

class Gui(
    override val name: String = "Gui",
    override val arity: Int = 0,
    override val superclass: PClass? = null,
    override val methods: HashMap<String, PCallable> = hashMapOf(
        "Window" to Window(),
        "Panel" to Panel(),
        "Label" to Label()
    ),
) : PNativeClass {

}