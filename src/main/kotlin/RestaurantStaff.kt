import kotlinx.coroutines.delay

/**
 * Restaurant staff are the only ones that can actually convert one Restaurant Item to another.
 *
 * However, they cannot do so without taking some time to perform their job.
 */

class Chef(val name: String, private val cookDelayMillis: Long = 2000L) {

    /**
     * Cook an [Order] into a [Meal] after a short time.
     */
    suspend fun cookOrder(order: Order): Meal {
        println("Chef $name: cooking $order…")
        delay(cookDelayMillis)
        println("Chef $name: finished cooking $order.")
        return Meal(order.dish, order.customer)
    }
}
