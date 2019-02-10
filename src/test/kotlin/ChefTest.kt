import io.kotlintest.TestCase
import io.kotlintest.matchers.beLessThan
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

class ChefTest : StringSpec() {

    private val mika = Customer("Mika", coroutineScope = GlobalScope)

    private val spaghettiDish = Dish("Spaghetti", 10)
    private val spaghettiOrder = Order(spaghettiDish, mika)
    private val spaghettiMeal = Meal(spaghettiDish, mika)

    private lateinit var nick: Chef
    private lateinit var emily: Chef

    override fun beforeTest(testCase: TestCase) {
        nick = Chef("Nick")
        emily = Chef("Emily", 1000L)
    }

    init {
        "name" {
            nick.name shouldBe "Nick"
            emily.name shouldBe "Emily"
        }

        "spaghetti order cooks as a spaghetti meal" {
            nick.cookOrder(spaghettiOrder) shouldBe spaghettiMeal
            emily.cookOrder(spaghettiOrder) shouldBe spaghettiMeal
        }

        "Emily cooks faster than Nick" {
            val nickCookAsync = async { measureTimeMillis { runBlocking { nick.cookOrder(spaghettiOrder) } } }
            val emilyCookAsync = async { measureTimeMillis { runBlocking { emily.cookOrder(spaghettiOrder) } } }

            val nickCookTime = nickCookAsync.await()
            val emilyCookTime = emilyCookAsync.await()

            emilyCookTime should beLessThan(nickCookTime)
        }

        "Nick and Emily cooking in parallel cooks faster than sequentially" {
            val parallelCooking = async {
                measureTimeMillis {
                    listOf(launch { nick.cookOrder(spaghettiOrder) }, launch { emily.cookOrder(spaghettiOrder) })
                        .forEach { it.join() }
                }
            }
            val sequentialCooking = async {
                measureTimeMillis {
                    launch { nick.cookOrder(spaghettiOrder) }.join()
                    launch { emily.cookOrder(spaghettiOrder) }.join()
                }
            }

            val parallelCookingTime = parallelCooking.await()
            val sequentialCookingTime = sequentialCooking.await()

            parallelCookingTime should beLessThan(sequentialCookingTime)
        }
    }
}
