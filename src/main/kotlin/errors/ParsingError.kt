package org.poach3r.errors

data class ParsingError(
    val line: Int,
    val msg: String,
    override val message: String = "$line - Parsing Error: $msg"
) : PError()