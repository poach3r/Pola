package org.poach3r.frontend.functions.gui.labels

import org.poach3r.frontend.Interpreter
import java.awt.Color
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel

class SetBackground(
    override val arity: Int = 2
) : LabelFunc {
    override fun call(interpreter: Interpreter, arguments: List<Any>): Any {
        val label = (arguments[0] as JLabel).apply {
            this.background = Color.decode(arguments[1].toString())
        }

        return getLabel(interpreter, label)
    }
}