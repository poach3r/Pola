package org.poach3r.frontend.functions.arrays

import org.poach3r.errors.RuntimeError
import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable
import org.poach3r.frontend.classes.Array
import org.poach3r.frontend.classes.String
import org.poach3r.frontend.functions.PFunction

interface ArrayFunc : PCallable {
    fun getList(obj: Any): ArrayList<Any> {
        if (obj is kotlin.String)
            return obj.toCharArray().toList().map(Char::toString) as ArrayList<Any>
        else
            return ArrayList<Any>(obj as ArrayList<Any>)
    }

    fun getIndex(list: ArrayList<Any>, obj: Any): Int {
        if (obj !is Double)
            throw RuntimeError(
                msg = "Cannot perform a set operation of non-number index '$obj' on array '$list'."
            )

        val index = obj.toInt()

        if (index >= list.size || index < 0)
            throw RuntimeError(
                msg = "Index '$index' of array '$list' is out of bounds (size is ${list.size})."
            )

        return index
    }

    fun getFunc(list: List<Any>, obj: Any): PFunction {
        if (obj !is PFunction)
            throw RuntimeError(
                msg = "Attempted to perform a foreeach operation with the non-function object '$obj' on the array '$list'."
            )

        return obj
    }

    fun getResult(list: List<Any>, arg: Any, interpreter: Interpreter): Any {
        if (arg is kotlin.String) {
            interpreter.globals.variables.get("string")?.let {
                return (it.value as String).call(interpreter, listOf(list.joinToString("")))
            }

            return list.joinToString("")
        }

        else
            return (interpreter.globals.variables.get("array")!!.value as Array).call(interpreter, list)
    }
}