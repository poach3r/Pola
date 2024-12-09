package org.poach3r.frontend.functions.sys

import org.poach3r.frontend.PCallable
import org.poach3r.frontend.PInstance
import org.poach3r.frontend.classes.PNativeClass

class MemoryInfo : PNativeClass {
    override val name: String = "MemoryInfo"
    override val arity: Int = 0
    override val methods: HashMap<String, PCallable> = hashMapOf()
    override val superclass: PNativeClass? = null

    override fun toString(fields: HashMap<String, Any>): String {
        return "MemoryInfo(max=${fields["max"]}, allocated=${fields["allocated"]}, free=${fields["free"]}, used=${fields["used"]})"
    }

    companion object {
        fun createInstance(max: Double, allocated: Double, free: Double, used: Double): PInstance {
            val instance = MemoryInfo()
            val fields = hashMapOf<String, Any>(
                "max" to max,
                "allocated" to allocated,
                "free" to free,
                "used" to used
            )
            return PInstance(instance, fields)
        }
    }

    fun bytesToMB(bytes: Long): Double = bytes.toDouble() / (1024 * 1024)
}