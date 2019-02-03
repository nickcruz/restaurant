/**
 * Items are the immutable artifacts created and passed around by people in the restaurant.
 */

/** Contains a maximum number of [seats] for seating [Customer]s. */
data class Table(val tableNumber: Int, val seats: Int, val seatedCustomers: Set<Customer>) {
    init {
        TODO("Implement dining experience first.")
    }

    fun hasAvailableSeat() = seats != seatedCustomers.size
}

/** Contains dishes that can be cooked into [Meal]s. */
data class Menu(val dishes: List<Dish>)

/** Gives details about the [name] of the dish and how much it [cost]s. */
data class Dish(val name: String, val cost: Int)

/** Contains a dish that was ordered by a [Customer]. */
data class Order(val dish: Dish, val customer: Customer)

/**
 * Customers may not be completely done with their dish when eating.
 *
 * Instead, ask them using [Customer.isFinishedEating].
 */
data class Meal(val dish: Dish, val customer: Customer)

/** Balance must be 0 before customers can leave. */
data class Bill(val orders: List<Order>, val balance: Int)
