package frontend.functions.gui.windows

import org.poach3r.frontend.Interpreter
import java.awt.Color
import javax.swing.JFrame

class SetForeground(
    override val arity: Int = 2
) : WindowFunc {
    override fun call(interpreter: Interpreter, arguments: List<Any>): Any {
        val frame = (arguments[0] as JFrame).apply {
            this.contentPane.foreground = Color.decode(arguments[1].toString())
        }

        return getPanel(interpreter, frame)
    }
}