package org.poach3r.frontend.functions.sys

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.PCallable
import org.poach3r.frontend.PInstance
import org.poach3r.frontend.classes.PNativeClass
import java.lang.management.ManagementFactory

class CPUInfo : PNativeClass {
    override val name: String = "CPUInfo"
    override val arity: Int = 0
    override val methods: HashMap<String, PCallable> = hashMapOf()
    override val superclass: PNativeClass? = null

    override fun toString(fields: HashMap<String, Any>): String {
        return "CPUInfo(cores=${fields["cores"]}, load_average=${fields["load_average"]})"
    }

    companion object {
        fun createInstance(cores: Int, loadAverage: Double): PInstance {
            val instance = CPUInfo()
            val fields = hashMapOf<String, Any>(
                "cores" to cores,
                "load_average" to loadAverage
            )
            return PInstance(instance, fields)
        }
    }
}


