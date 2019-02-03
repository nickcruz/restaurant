import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.Closeable

/**
 * Cooks food after a short time.
 */
class Chef(private val orders: ReceiveChannel<Order>, private val meals: SendChannel<Meal>) : Closeable {

    private var job: Job? = null

    fun startCooking() {
        close()
        job = GlobalScope.launch {
            for (order in orders) {
                meals.send(cookOrder(order))
            }
        }
    }

    override fun close() {
        job?.cancel()
    }

    private suspend fun cookOrder(order: Order): Meal {
        println("Cooking $order...")
        delay(COOK_DELAY)
        println("\tFinished cooking $order.")
        return Meal(order.dish, order.customer)
    }

    companion object {
        private const val COOK_DELAY = 2000L
    }
}
