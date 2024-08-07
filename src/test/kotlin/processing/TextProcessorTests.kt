package processing

import assertEqualIgnoreOrder
import com.voze.mtolman.processing.TextDictionary
import com.voze.mtolman.processing.TextProcessor
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class TextProcessorTests {
    companion object {
        val testTextDictionary = TextDictionary(listOf(
            "car",
            "cat",
            "cab",
            "cart",
            "cars"
        ))

        val testProcessor = TextProcessor(testTextDictionary)
    }

    @Test
    fun testFindMisspellingsMultipleWords() {
        assertEquals(listOf(), testProcessor.findMisspellings("car cat cAb CaRT CARS"))

        assertMisspelledWords("Cars card cart cats", "card", "cats")
        assertSuggestions("Cars card cart cats", 3, "cart", "car", "cars", "cars", "cat", "cab")
    }

    @Test
    fun testFindMisspellingsMultipleWordsPunctuation() {
        assertEquals(listOf(), testProcessor.findMisspellings("car (12 - cat), cAb.;CaRT!CARS?"))

        assertMisspelledWords("Cars, card. cart: cats", "card", "cats")
        assertSuggestions("Cars card cart cats", 3, "cart", "car", "cars", "cars", "cat", "cab")
    }

    @Test
    fun testFindMisspellingsSingleWord() {
        assertEquals(listOf(), testProcessor.findMisspellings("Car"))

        assertMisspelledWords("card", "card")
        assertSuggestions("card", 3, "car", "cart", "cars")
    }

    private fun assertSuggestions(text: String, maxSuggestions: Int, vararg misspellings: String) {
        val res = testProcessor.findMisspellings(text, maxSuggestions).flatMap { it.suggestions.map { inner -> inner.correction } }
        assertEqualIgnoreOrder(misspellings.toList(), res)
    }

    private fun assertMisspelledWords(text: String, vararg misspellings: String) {
        val res = testProcessor.findMisspellings(text).map { it.misspelled }
        assertContentEquals(misspellings.toList(), res)
    }
}