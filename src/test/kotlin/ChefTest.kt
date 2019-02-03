import io.kotlintest.TestCase
import io.kotlintest.TestResult
import io.kotlintest.data.forall
import io.kotlintest.matchers.numerics.shouldBeGreaterThan
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlin.system.measureTimeMillis

class ChefTest : StringSpec() {

    private val testTimeout = 5000L

    private val customer = Customer()

    private val spaghettiDish = Dish("Spaghetti", 10)
    private val spaghettiOrder = Order(spaghettiDish, customer)
    private val spaghettiMeal = Meal(spaghettiDish, customer)

    private val adoboDish = Dish("Adobo", 8)
    private val adoboOrder = Order(adoboDish, customer)
    private val adoboMeal = Meal(adoboDish, customer)

    private val chefsCount = 2

    private lateinit var orders: Channel<Order>
    private lateinit var meals: Channel<Meal>

    private lateinit var chefs: List<Chef>

    override fun beforeTest(testCase: TestCase) {
        orders = Channel()
        meals = Channel(UNLIMITED) // Assume infinite waiters. TODO: Add unit tests for having less waiters.

        chefs = List(chefsCount) { autoClose(Chef(orders, meals)) }
    }

    override fun afterTest(testCase: TestCase, result: TestResult) {
        orders.close()
        meals.close()

        closeResources()
    }

    init {
        "sending order cooks meal" {
            forall(
                row(spaghettiOrder, spaghettiMeal),
                row(adoboOrder, adoboMeal)
            ) { order, meal ->
                chefs.map { it.startCooking() }
                runBlocking {
                    withTimeout(testTimeout) {
                        orders.send(order)
                        withContext(Dispatchers.Default) { meals.receive() } shouldBe meal
                    }
                }
            }
        }

        "two cooks cook faster than one" {
            val cookTwoMeals = suspend {
                orders.send(spaghettiOrder)
                orders.send(adoboOrder)
                meals.receive()
                meals.receive()
            }
            chefs[0].startCooking()
            val oneCook = measureTimeMillis {
                runBlocking {
                    withTimeout(testTimeout) {
                        cookTwoMeals()
                    }
                }
            }

            chefs[1].startCooking()
            val twoCooks = measureTimeMillis {
                runBlocking {
                    withTimeout(testTimeout) {
                        cookTwoMeals()
                    }
                }
            }

            oneCook shouldBeGreaterThan twoCooks
        }
    }
}
