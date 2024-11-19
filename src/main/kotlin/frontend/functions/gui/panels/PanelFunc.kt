package org.poach3r.frontend.functions.gui.panels

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable
import org.poach3r.frontend.PInstance
import org.poach3r.frontend.classes.gui.Panel
import javax.swing.JPanel

interface PanelFunc : PCallable {
    fun getPanel(interpreter: Interpreter, panel: JPanel): Any {
        return ((interpreter.globals.variables.get("gui")!!.value as PInstance).get("Panel") as Panel).call(
            interpreter,
            listOf(panel)
        )
    }
}