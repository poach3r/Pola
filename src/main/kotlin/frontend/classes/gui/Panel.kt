package org.poach3r.frontend.classes.gui

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable
import org.poach3r.frontend.PInstance
import org.poach3r.frontend.classes.PClass
import org.poach3r.frontend.classes.PNativeClass
import org.poach3r.frontend.functions.gui.panels.Add
import org.poach3r.frontend.functions.gui.panels.SetBackground
import org.poach3r.frontend.functions.gui.panels.SetForeground
import org.poach3r.frontend.functions.gui.panels.Show
import javax.swing.JPanel
import kotlin.String

class Panel(
    override val name: String = "Panel",
    override val arity: Int = 0,
    override val superclass: PClass? = null,
    override val methods: HashMap<String, PCallable> = hashMapOf(
        "setBackground" to SetBackground(),
        "setForeground" to SetForeground(),
        "show" to Show(),
        "add" to Add()
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
                            JPanel()
                        else
                            arguments[0] as JPanel
            )
        )
    }
}