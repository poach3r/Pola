package org.poach3r.frontend.functions

import org.poach3r.errors.RuntimeError
import org.poach3r.frontend.PCallable

interface DealsWithAnonymousFunctions: PCallable {
    fun getFunc(list: List<Any>, index: Int): PFunction {
        if (list[index] !is PFunction)
            throw RuntimeError(
                msg = "Attempted to perform a foreeach operation with the non-function object '${list[index]}' on the array '$list'."
            )

        return list[index] as PFunction
    }
}