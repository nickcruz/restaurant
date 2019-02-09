import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * Our Restaurant launches all of the coroutines that will make the restaurant run. Hiring waitstaff or chefs launch
 * coroutines that wait for things like incoming customers or cooked meals.
 */
class Restaurant(setUpRestaurant: suspend Restaurant.() -> Unit) : CoroutineScope {

    override val coroutineContext: CoroutineContext = Job()

    private val chefOrders = Channel<Order>()
    private val chefMeals = Channel<Meal>(UNLIMITED) // For now, assume infinite waiters.

    /**
     * Hires a chef. Chefs require no training and begin cooking immediately if there are orders to cook.
     */
    fun hireChef(
        name: String,
        orders: ReceiveChannel<Order> = chefOrders,
        meals: SendChannel<Meal> = chefMeals
    ) = launch {
        for (order in orders) {
            println("$name is cooking $order...")
            delay(COOK_DELAY)
            println("\t$name finished cooking $order.")
            meals.send(Meal(order.dish, order.customer))
        }
    }

    /**
     * Welcomes a group of [Customer]s. A group of customers will want to sit with each other at the same table.
     */
    fun welcome(customers: List<Customer>) {
        TODO("Implement")
    }

    /**
     * Closes the restaurant at the end of the day.
     */
    fun closeRestaurant() {
        coroutineContext.cancel()
    }
}
