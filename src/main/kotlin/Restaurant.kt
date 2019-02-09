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
 * Our Restaurant is a [CoroutineContext] containing all of the child coroutines that make the restaurant run and serve
 * customers.
 *
 * <p>Hiring waitstaff or chefs (or anyone else that works at the restaurant) launches coroutines in the context of the
 * restaurant. When the restaurant closes for the day, all workers should also be closed for the day.
 */
class Restaurant : CoroutineScope {

    override val coroutineContext: CoroutineContext = Job()

    private val chefOrders = Channel<Order>()
    private val chefMeals = Channel<Meal>(UNLIMITED) // For now, assume infinite waiters.

    /**
     * Welcomes a group of [Customer]s. A group of customers will want to sit with each other at the same table.
     *
     * Customers are not coroutines--they don't have any work to do. That's the [Job] of many different other
     * coroutines to keep track of where Customers are in their dining experience.
     *
     * TODO: Implement the Customer dining experience.
     */
    fun welcome(customers: List<Customer>) {
        TODO("Implement")
    }

    /**
     * Hires a chef. Chefs require no training and begin cooking immediately if there are orders to cook. Otherwise,
     * chefs will *suspend* their work until they are given a new order to cook.
     *
     * Hiring a chef attaches a coroutine to the restaurant coroutine.
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
     * Closes the restaurant at the end of the day.
     */
    fun closeRestaurant() {
        coroutineContext.cancel()
    }

    companion object {
        private const val COOK_DELAY = 2000L
    }
}
