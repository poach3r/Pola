package org.poach3r.frontend.classes.gui

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable
import org.poach3r.frontend.PInstance
import org.poach3r.frontend.classes.PClass
import org.poach3r.frontend.classes.PNativeClass
import org.poach3r.frontend.functions.gui.labels.SetBackground
import org.poach3r.frontend.functions.gui.labels.SetForeground
import org.poach3r.frontend.functions.gui.labels.SetText
import javax.swing.JLabel

class Label(
    override val name: String = "Label",
    override val arity: Int = 0,
    override val superclass: PClass? = null,
    override val methods: HashMap<String, PCallable> = hashMapOf(
        "setText" to SetText(),
        "setForeground" to SetForeground(),
        "setBackground" to SetBackground()
    ),
) : PNativeClass {
    override fun call(
        interpreter: Interpreter,
        arguments: List<Any>
    ): Any {
        return PInstance(
            clazz = this,
            fields = hashMapOf(
                "__literalValue" to
                        if (arguments.isEmpty())
                            JLabel()
                        else
                            arguments[0] as JLabel
            )
        )
    }
}