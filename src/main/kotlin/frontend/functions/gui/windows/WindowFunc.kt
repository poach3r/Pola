package frontend.functions.gui.windows

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable
import org.poach3r.frontend.PInstance
import org.poach3r.frontend.classes.gui.Window
import javax.swing.JFrame

interface WindowFunc : PCallable {
    fun getPanel(interpreter: Interpreter, frame: JFrame): Any {
        return ((interpreter.globals.variables.get("gui")!!.value as PInstance).get("Window") as Window).call(
            interpreter,
            listOf(frame)
        )
    }
}