package org.poach3r.frontend.classes.gui

import frontend.functions.gui.windows.Add
import frontend.functions.gui.windows.SetBackground
import frontend.functions.gui.windows.SetForeground
import frontend.functions.gui.windows.Show
import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable
import org.poach3r.frontend.PInstance
import org.poach3r.frontend.classes.PClass
import org.poach3r.frontend.classes.PNativeClass
import javax.swing.JFrame
import javax.swing.WindowConstants.EXIT_ON_CLOSE
import kotlin.String

class Window(
    override val name: String = "Window",
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
            fields = hashMapOf("__literalValue" to
                    if (arguments.isEmpty())
                        JFrame().apply { this.defaultCloseOperation = EXIT_ON_CLOSE }
                    else
                        arguments[0] as JFrame
            )
        )
    }
}