package frontend.functions.gui.windows

import org.poach3r.frontend.Interpreter
import javax.swing.JFrame

class Show(
    override val arity: Int = 1
) : WindowFunc {
    override fun call(interpreter: Interpreter, arguments: List<Any>): Any {
        val frame = (arguments[0] as JFrame).apply {
            this.pack()
            this.isVisible = true
        }

        return getPanel(interpreter, frame)
    }
}