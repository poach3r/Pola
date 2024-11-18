package org.poach3r.frontend.classes

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable
import org.poach3r.frontend.PInstance
import org.poach3r.frontend.functions.io.files.Create
import org.poach3r.frontend.functions.io.files.Delete
import org.poach3r.frontend.functions.io.files.Exists
import org.poach3r.frontend.functions.io.files.IsFile
import org.poach3r.frontend.functions.io.files.Read
import org.poach3r.frontend.functions.io.files.Write
import java.io.File
import kotlin.String

class File(
    override val name: String = "File",
    override val arity: Int = 1,
    override val methods: HashMap<String, PCallable> = hashMapOf(
        "read" to Read(),
        "isFile" to IsFile(),
        "exists" to Exists(),
        "delete" to Delete(),
        "create" to Create(),
        "write" to Write()
    ),
    override val superclass: PClass? = null,
) : PNativeClass {
    override fun toString(fields: HashMap<String, Any>): String {
        return fields.get("__literalValue").toString()
    }

    override fun call(
        interpreter: Interpreter,
        arguments: List<Any>
    ): Any {
        return PInstance(
            clazz = this,
            fields = hashMapOf("__literalValue" to File(arguments[0].toString()))
        )
    }
}