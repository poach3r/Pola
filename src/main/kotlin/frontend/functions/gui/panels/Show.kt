package org.poach3r.frontend.functions.gui.panels

import org.poach3r.frontend.Interpreter
import javax.swing.JFrame
import javax.swing.JPanel

class Show(
    override val arity: Int = 1
) : PanelFunc {
    override fun call(interpreter: Interpreter, arguments: List<Any>): Any {
        val panel = (arguments[0] as JPanel).apply {
            this.isVisible = true
        }

        return getPanel(interpreter, panel)
    }
}