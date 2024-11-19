package org.poach3r.frontend.functions.gui.labels

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable
import org.poach3r.frontend.PInstance
import org.poach3r.frontend.classes.gui.Label
import org.poach3r.frontend.classes.gui.Panel
import javax.swing.JLabel
import javax.swing.JPanel

interface LabelFunc : PCallable {
    fun getLabel(interpreter: Interpreter, label: JLabel): Any {
        return ((interpreter.globals.variables.get("gui")!!.value as PInstance).get("Label") as Label).call(interpreter, listOf(label))
    }
}