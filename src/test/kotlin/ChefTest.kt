import io.kotlintest.TestCase
import io.kotlintest.TestResult
import io.kotlintest.data.forall
import io.kotlintest.matchers.numerics.shouldBeGreaterThan
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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

    private lateinit var testOrders: Channel<Order>
    private lateinit var testMeals: Channel<Meal>

    private lateinit var restaurant: Restaurant

    override fun beforeTest(testCase: TestCase) {
        testOrders = Channel()
        testMeals = Channel(UNLIMITED)

        restaurant = Restaurant {
            hireChef("Adam", testOrders, testMeals)
            hireChef("Emily", testOrders, testMeals)
            hireChef("Nick", testOrders, testMeals)
            hireChef("Noah", testOrders, testMeals)
        }
    }

    override fun afterTest(testCase: TestCase, result: TestResult) {
        restaurant.closeRestaurant()
    }

    init {
        "sending order cooks meal" {
            forall(
                row(spaghettiOrder, spaghettiMeal),
                row(adoboOrder, adoboMeal)
            ) { order, meal ->
                runBlocking {
                    testOrders.send(order)
                    withTimeout(testTimeout) {
                        testMeals.receive()
                    } shouldBe meal
                }
            }
        }

        "two cooks cook faster than one" {
            val cookTwoMeals = suspend {
                testOrders.send(spaghettiOrder)
                testOrders.send(adoboOrder)
                testMeals.receive()
                testMeals.receive()
            }

            // Create a restaurant with one chef.
            val oneCook = measureTimeMillis {
                runBlocking {
                    withTimeout(testTimeout) {
                        cookTwoMeals()
                    }
                }
            }

            // Create a new restaurant with more chefs.
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
