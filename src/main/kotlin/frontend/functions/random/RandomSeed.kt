package frontend.functions.random;

import org.poach3r.frontend.Interpreter
import org.poach3r.frontend.classes.RandomFunc
import kotlin.random.Random

class RandomSeed : RandomFunc() {
    private var sharedRandom: Random? = null

    override val arity: Int = 0

    override fun call(
        interpreter: Interpreter,
        arguments: List<Any>
    ): Any {
        val seed = if (arguments.isNotEmpty() && arguments[0] is Double) {
            (arguments[0] as Double).toLong()
        } else {
            System.currentTimeMillis()
        }

        sharedRandom = Random(seed)
        return seed.toDouble()
    }
}
