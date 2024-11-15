package org.poach3r.errors

class ArgError(
    val index: Int,
    val msg: String,
    override val message: String = "$index - Argument Error: $msg"
) : PError()
