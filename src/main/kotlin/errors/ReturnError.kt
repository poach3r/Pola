package org.poach3r.errors

data class ReturnError(
    val value: Any
): RuntimeException()