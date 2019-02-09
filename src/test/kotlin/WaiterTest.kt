import io.kotlintest.TestCase
import io.kotlintest.matchers.string.contain
import io.kotlintest.should
import io.kotlintest.shouldThrow
import io.kotlintest.specs.StringSpec

class WaiterTest : StringSpec() {

    private val mika = Customer("Mika")

    private lateinit var nick: Waiter

    override fun beforeTest(testCase: TestCase) {
        nick = Waiter("Nick")
    }

    init {
        "already serving nick throws exception" {
            nick.seatCustomer(mika)

            val exception = shouldThrow<IllegalStateException> {
                nick.seatCustomer(mika)
            }
            exception.message should contain("customer")
        }
    }
}

