package org.poach3r.frontend.functions.gui.panels

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PInstance
import javax.swing.JComponent
import javax.swing.JPanel

class Add(
    override val arity: Int = -1
) : PanelFunc {
    override fun call(interpreter: Interpreter, arguments: List<Any>): Any {
        val panel = arguments[0] as JPanel

        arguments.toMutableList().apply{
            this.removeAt(0)
        }.map {
            (it as PInstance).get() as JComponent
        }.forEach {
            panel.add(it)
        }

        return getPanel(interpreter, panel)
    }
}