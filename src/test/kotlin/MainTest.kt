import org.poach3r.Global
import org.poach3r.Main
import kotlin.test.Test

class MainTest {
    @Test
    fun testPresidents() {
        test("-f", "examples/presidents.pola")
    }

    @Test
    fun testRand() {
        test("-f", "examples/rand.pola")
    }

    @Test
    fun testFib() {
        test("-f", "examples/fib.pola")
    }

    fun test(vararg args: String) {
        Global.main = Main(arrayOf(*args))
        Global.main.interpret()
    }
}