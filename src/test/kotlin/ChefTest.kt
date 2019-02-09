import io.kotlintest.TestCase
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class ChefTest : StringSpec() {

    private val testTimeout = 5000L

    private val customer = Customer()

    private val spaghettiDish = Dish("Spaghetti", 10)
    private val spaghettiOrder = Order(spaghettiDish, customer)
    private val spaghettiMeal = Meal(spaghettiDish, customer)

    private val adoboDish = Dish("Adobo", 8)
    private val adoboOrder = Order(adoboDish, customer)
    private val adoboMeal = Meal(adoboDish, customer)

    private lateinit var nick: Chef
    private lateinit var emily: Chef

    override fun beforeTest(testCase: TestCase) {
        nick = Chef("Nick")
        emily = Chef("Emily", 1000L)
    }

    init {
        "name" {
            nick.name shouldBe "Nick"
            emily.name shouldBe "Emily"
        }
    }
}
