package frontend.functions.gui.windows

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PInstance
import javax.swing.JComponent
import javax.swing.JFrame

class Add(
    override val arity: Int = -1
) : WindowFunc {
    override fun call(interpreter: Interpreter, arguments: List<Any>): Any {
        val frame = arguments[0] as JFrame

        arguments.toMutableList().apply {
            this.removeAt(0)
        }.map {
            (it as PInstance).get() as JComponent
        }.forEach {
            frame.contentPane.add(it)
        }

        return getPanel(interpreter, frame)
    }
}