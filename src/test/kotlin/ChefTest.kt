import io.kotlintest.TestCase
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class ChefTest : StringSpec() {

    lateinit var word: String

    override fun beforeTest(testCase: TestCase) {
        word = "Hello"
    }

    init {
        "word length 5" {
            word.length shouldBe 5
        }

        "word is \"Hello\"" {
            word shouldBe "Hello"
        }
    }
}
