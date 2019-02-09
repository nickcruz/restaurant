/**
 * Dines in the [Restaurant] and causes most of the waiting time for the waitstaff and cooks.
 *
 * Ideally, the restaurant should be relatively impervious to any delays that a customer will impose on the waitstaff.
 *
 * Write unit tests configuring number of waitstaff, tables, restaurants, etc. to account for the highest number of
 * Customers.
 */
class Customer {

    /**
     * Sits down at a table if there's an available seat. Otherwise, the customer complains.
     */
    fun sitAt(table: Table): Table {
        TODO("Implement dining experience first.")
    }

    /**
     * Peruses a [Menu] and gives an [Order] when ready.
     */
    fun order(menu: Menu): Order {
        TODO("Implement")
    }

    /**
     * Eats a meal.
     */
    fun dine(meal: Meal) {
        TODO("Implement")
    }

    /**
     * Has to be asked if they are finished eating before giving a bill.
     */
    fun isFinishedEating(): Boolean {
        TODO("Implement")
    }

    fun pay(order: Order, bill: Bill): Bill {
        TODO("Implement")
    }

    fun leave() {

    }

    class CustomerCompliant(complaint: String) : RuntimeException(complaint)

    companion object {
        const val EAT_DELAY = 10_000L
    }
}
