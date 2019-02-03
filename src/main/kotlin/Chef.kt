import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val COOK_DELAY = 2000L

/**
 * Cooks food after a short time.
 */
// TODO: Extend Restaurant instead and build the restaurant using type-safe builders.
// https://kotlinlang.org/docs/reference/type-safe-builders.html#type-safe-builders
fun CoroutineScope.hireChef(orders: ReceiveChannel<Order>, meals: SendChannel<Meal>) = launch {
    for (order in orders) {
        println("Cooking $order...")
        delay(COOK_DELAY)
        println("\tFinished cooking $order.")
        meals.send(Meal(order.dish, order.customer))
    }
}
