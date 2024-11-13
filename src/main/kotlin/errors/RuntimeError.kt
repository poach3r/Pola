package org.poach3r.errors

data class RuntimeError(
    val line: Int? = null,
    val msg: String,
    override val message: String = "$line - Runtime Error: $msg"
): PError()