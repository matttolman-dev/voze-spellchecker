import com.voze.mtolman.TextDictionary
import com.voze.mtolman.Results.WordResult
import kotlin.test.Test
import kotlin.test.assertEquals

class TextDictionaryTests {
    companion object {
        val testTextDictionary = TextDictionary(listOf("cat", "car", "cab", "bar", "far", "ode", "bike", "trick", "large", "small", "snail"))
    }

    @Test
    fun testFindExact() {
        assertEquals(listOf(), testTextDictionary.misspelledNearest("car"))
        assertEquals(listOf(), testTextDictionary.misspelledNearest("snail"))
    }

    @Test
    fun testFindClosest() {
        assertEqualIgnoreOrder(listOf(
            WordResult("car", 1),
            WordResult("bar", 1),
            WordResult("far", 1),
        ), testTextDictionary.misspelledNearest("tar", 3))

        assertEqualIgnoreOrder(listOf(
            WordResult("car", 1),
            WordResult("bar", 1),
            WordResult("far", 1),
            WordResult("cat", 2),
            WordResult("cab", 2),
        ), testTextDictionary.misspelledNearest("tar", 5))

        assertEqualIgnoreOrder(listOf(
            WordResult("car", 1),
            WordResult("bar", 1),
            WordResult("far", 1),
            WordResult("cat", 2),
            WordResult("cab", 2),
            WordResult("ode", 3),
            WordResult("large", 3),
        ), testTextDictionary.misspelledNearest("tar", 7))
    }
}