import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * Every customer has their own parameters for how long they take to order a dish and how long they take to eat.
 *
 * Only they know how long it takes them for these tasks respectively. This is intentional and lets us simulate delays
 * for which we have no control over, even though we have full control of how long it might take the customer.
 */
class Customer(
    val name: String,
    private val orderDecisionTime: Long = 2000L,
    private val eatingTime: Long = 2000L,
    private val coroutineScope: CoroutineScope
) {

    var order: Order? = null
        private set
    var finishedEating: Boolean = false
        private set

    private var decidingOrder: Job? = null
    private var eatingMeal: Job? = null

    /**
     * Gives the customer a menu so they can decide on an order. Check back periodically to get this Customer's order.
     */
    fun decideOrder(menu: Menu) {
        if (decidingOrder != null) {
            throw IllegalStateException("Already deciding or has decided on order.")
        }
        println("Customer $name: perusing $menu…")
        decidingOrder = coroutineScope.launch {
            delay(orderDecisionTime)
            val dish = menu.dishes[Random.nextInt(0, menu.dishes.size)]
            println("Customer $name: has decided on $dish.")
            order = Order(dish, this@Customer)
        }
    }

    fun eatMeal(meal: Meal) {
        if (eatingMeal != null) {
            throw IllegalStateException("Already eating meal.")
        }
        eatingMeal = coroutineScope.launch {
            println("Customer $name: eating their $meal…")
            delay(eatingTime)
            println("Customer $name: finished eating their $meal.")
            finishedEating = true
        }
    }
}
