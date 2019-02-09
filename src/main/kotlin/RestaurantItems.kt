/**
 * Items are the immutable artifacts created and passed around by people in the restaurant.
 */

/** Gives details about the [name] of the dish and how much it [cost]s. */
data class Dish(val name: String, val cost: Int)

/** Uncooked dish to be cooked by a [Chef]. */
data class Order(val dish: Dish, val customer: Customer)

/** Cooked dish to be served to a [Customer]. */
data class Meal(val dish: Dish, val customer: Customer)
