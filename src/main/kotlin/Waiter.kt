import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * Waiters serve as the conduit between a [Customer] and their food. They're essentially a messenger for the customer to
 * tell the Chef what they want to be cooked.
 *
 * The only difference is that Customers are not allowed to talk unless the Waiter approaches them. Otherwise, they are
 * simply talking between other Customers at the table.
 *
 * This means a waiter must ask the customer every once in a while whether or not they're ready to order or finished
 * eating--walking to their table takes some time, as does walking back.
 */
class Waiter(
    val name: String,
    private val walkingDelay: Long = 200L,
    private val checkInDelay: Long = 500L,
    private val coroutineScope: CoroutineScope,
    private val sendOrder: suspend (Order) -> Meal
) {

    // TODO: Figure out how to set these so we don't seat a customer we don't need...but maybe a Channel will prevent this?
    private var customerOrder: Deferred<Order>? = null
    private var customerEating: Job? = null

    /**
     * Seat a [Customer] at a table.
     *
     * For now, assume only 1 customer at a time can sit at a table.
     */
    suspend fun seatCustomer(customer: Customer) {
        if (customerOrder != null || customerEating != null) {
            throw IllegalStateException("Currently serving a customer.")
        }
        println("Waiter $name: seating $customer…")
        delay(walkingDelay)
        println("Waiter $name: seated $customer.")

        val order = waitForCustomerOrder(customer)
        val meal = withContext(coroutineScope.coroutineContext) { sendOrder(order) }
        waitForCustomerToFinishEating(customer, meal)
    }

    private suspend fun waitForCustomerOrder(customer: Customer): Order {
        customer.decideOrder(Restaurant.MENU)
        walkBackToStaffArea()
        println("Waiter $name: Waiting for order from $customer…")
        var order: Order? = null
        while ((order == null).also { waitForCustomer() }) {
            walkToTable()
            order = customer.order
            walkBackToStaffArea()
            if (order != null) {
                println("Waiter $name: \tgot an $order from $customer.")
            } else {
                println("Waiter $name: \tno decision yet from $customer.")
            }
        }
        if (order == null) {
            // TODO: Figure out how to continue without this exception.
            throw IllegalStateException("Should not get to this path.")
        }
        return order
    }

    private suspend fun waitForCustomerToFinishEating(customer: Customer, meal: Meal) {
        customer.eatMeal(meal)
        walkBackToStaffArea()
        println("Waiter $name: Waiting for $customer to finish eating…")
        var finishedEating = false
        while (!finishedEating.also { waitForCustomer() }) {
            walkToTable()
            finishedEating = customer.finishedEating
            walkBackToStaffArea()
            if (finishedEating) {
                println("Waiter $name: \tsaw that $customer finished eating their $meal.")
            } else {
                println("Waiter $name: \tsaw that $customer still eating their meal.")
            }
        }
    }

    private suspend fun walkToTable() {
        delay(walkingDelay)
    }

    private suspend fun walkBackToStaffArea() {
        delay(walkingDelay)
    }

    private suspend fun waitForCustomer() {
        delay(checkInDelay)
    }
}
