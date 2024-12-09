package org.poach3r.frontend.functions.sys

import org.poach3r.frontend.PCallable
import org.poach3r.frontend.PInstance
import org.poach3r.frontend.classes.PNativeClass

class DiskInfo : PNativeClass {
    override val name: String = "DiskInfo"
    override val arity: Int = 0
    override val methods: HashMap<String, PCallable> = hashMapOf()
    override val superclass: PNativeClass? = null

    override fun toString(fields: HashMap<String, Any>): String {
        return "DiskInfo(total_space=${fields["total_space"]}, free_space=${fields["free_space"]}, usable_space=${fields["usable_space"]})"
    }

    companion object {
        fun createInstance(totalSpace: Double, freeSpace: Double, usableSpace: Double): PInstance {
            val instance = DiskInfo()
            val fields = hashMapOf<String, Any>(
                "total_space" to totalSpace,
                "free_space" to freeSpace,
                "usable_space" to usableSpace
            )
            return PInstance(instance, fields)
        }
    }
}
