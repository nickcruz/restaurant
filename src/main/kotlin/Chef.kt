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

    // TODO: Maybe it'd even be easier to imagine if Chef implemented the job, so that we have a 1:1 object:coroutine ratio.
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
        print("Cooking $order...")
        delay(COOK_DELAY)
        print("Done.\n")
        return Meal(order.dish, order.customer)
    }

    companion object {
        const val COOK_DELAY = 2000L
    }
}
