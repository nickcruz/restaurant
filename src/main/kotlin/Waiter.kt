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
class Waiter(val name: String, private val walkingDelay: Long = 500L) {

    private var currentCustomer: Customer? = null

    /**
     * Seat a [Customer] at a table.
     *
     * For now, assume only 1 customer at a time can sit at a table.
     */
    fun seatCustomer(customer: Customer) {
        if (currentCustomer != null) {
            throw IllegalStateException("Currently serving a customer.")
        }
        currentCustomer = customer
    }
}
