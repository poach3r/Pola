package org.poach3r.frontend.functions.gui.panels

import org.poach3r.frontend.Interpreter
import java.awt.Color
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel

class SetBackground(
    override val arity: Int = 2
) : PanelFunc {
    override fun call(interpreter: Interpreter, arguments: List<Any>): Any {
        val panel = (arguments[0] as JPanel).apply {
            this.background = Color.decode(arguments[1].toString())
        }

        return getPanel(interpreter, panel)
    }
}