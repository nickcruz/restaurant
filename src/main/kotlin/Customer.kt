import kotlinx.coroutines.delay
import kotlin.random.Random

/**
 * Every customer has their own parameters for how long they take to order a dish and how long they take to eat.
 *
 * Only they know how long it takes them for these tasks respectively.
 */
class Customer(val name: String, private val orderDecisionTime: Long = 2000L, private val eatingTime: Long = 2000L) {

    suspend fun decideOrder(menu: Menu): Dish {
        println("Customer $name: perusing $menu…")
        delay(orderDecisionTime)
        val dish = menu.dishes[Random.nextInt(0, menu.dishes.size)]
        println("Customer $name: will order $dish.")
        return dish
    }

    suspend fun eatMeal(meal: Meal) {
        println("Customer $name: eating their $meal…")
        delay(eatingTime)
        println("Customer $name: finished eating their $meal.")
    }
}
