import com.voze.mtolman.Dictionary
import com.voze.mtolman.WordResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DictionaryTests {
    companion object {
        val testDictionary = Dictionary(listOf("cat", "car", "cab", "bar", "far", "ode", "bike", "trick", "large", "small", "snail"))
    }

    @Test
    fun testFindExact() {
        assertEquals(listOf(WordResult("car", 0)), testDictionary.nearest("car"))
        assertEquals(listOf(WordResult("snail", 0)), testDictionary.nearest("snail"))
    }

    @Test
    fun testFindClosest() {
        assertEqualIgnoreOrder(listOf(
            WordResult("car", 1),
            WordResult("bar", 1),
            WordResult("far", 1),
        ), testDictionary.nearest("tar", 3))

        assertEqualIgnoreOrder(listOf(
            WordResult("car", 1),
            WordResult("bar", 1),
            WordResult("far", 1),
            WordResult("cat", 2),
            WordResult("cab", 2),
        ), testDictionary.nearest("tar", 5))

        assertEqualIgnoreOrder(listOf(
            WordResult("car", 1),
            WordResult("bar", 1),
            WordResult("far", 1),
            WordResult("cat", 2),
            WordResult("cab", 2),
            WordResult("ode", 3),
            WordResult("large", 3),
        ), testDictionary.nearest("tar", 7))
    }

    fun <T> assertEqualIgnoreOrder(expected: List<T>, actual: List<T>) {
        assertEquals(expected.size, actual.size)
        assertTrue(expected.containsAll(actual), "Expected ${expected.joinToString(",")} to equal ${actual.joinToString(",")}")
        assertTrue(actual.containsAll(expected))
    }
}