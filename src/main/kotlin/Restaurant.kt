import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
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
     * Hires a chef. Chefs require no training (for now) and begin cooking immediately if there are orders to cook.
     *
     * Hiring a chef attaches a coroutine to the restaurant coroutine.
     *
     * TODO: Not sure how to test this quite yet or whether or not this is the best API for hiring a Chef.
     */
    fun hireChef(
        chef: Chef,
        orders: ReceiveChannel<Order> = chefOrders,
        meals: SendChannel<Meal> = chefMeals
    ) = launch {
        for (order in orders) {
            chef.cookOrder(order)
            meals.send(Meal(order.dish, order.customer))
        }
    }

    companion object {
        val MENU =
            Menu(
                Dish("Spaghetti", 15),
                Dish("Adobo", 8),
                Dish("Pad Thai", 12),
                Dish("Spicy Miso Ramen", 22)
            )
    }
}
