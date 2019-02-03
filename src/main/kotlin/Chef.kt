import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.delay

class Chef(private val kitchenCounter: KitchenCounter) {

    suspend fun cookOrders() {
        for (order in kitchenCounter) {
            print("Cooking $order...")
            delay(COOK_DELAY)
            print("Done.\n")
            kitchenCounter.send(Meal(order.dish, order.customer))
        }
    }

    companion object {
        const val COOK_DELAY = 2000L
    }
}

class KitchenCounter(private val orders: ReceiveChannel<Order>, private val meals: SendChannel<Meal>) :
    ReceiveChannel<Order> by orders, SendChannel<Meal> by meals
