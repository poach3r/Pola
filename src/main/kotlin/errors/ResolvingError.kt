package org.poach3r.errors

data class ResolvingError(
    val line: Int,
    val msg: String,
    override val message: String = "$line - Resolving Error: $msg"
) : PError()