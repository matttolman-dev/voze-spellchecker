package com.voze.mtolman.text

import com.voze.mtolman.assertEqualIgnoreOrder
import com.voze.mtolman.com.voze.mtolman.text.dictionaries.TrieDictionary
import com.voze.mtolman.com.voze.mtolman.text.dictionaries.ListDictionary
import com.voze.mtolman.com.voze.mtolman.text.TextProcessor
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class TextProcessorTests {
    companion object {
        val testListDictionary = ListDictionary(listOf(
            "car",
            "cat",
            "cab",
            "cart",
            "cars"
        ))
        val testTrieDictionary = TrieDictionary(listOf(
            "car",
            "cat",
            "cab",
            "cart",
            "cars"
        ))
        val dictionaries = listOf(testListDictionary, testTrieDictionary)
    }

    @Test
    fun testFindMisspellingsMultipleWords() {
        dictionaries.forEach { dict ->
            val testProcessor = TextProcessor(testListDictionary)
            assertEquals(listOf(), testProcessor.findMisspellings("car cat cAb CaRT CARS"))

            assertMisspelledWords("Cars card cart cats", "card", "cats")
            assertSuggestions("Cars card cart cats", 3, "cart", "car", "cars", "cars", "cat", "cab")
        }
    }

    @Test
    fun testFindMisspellingsMultipleWordsPunctuation() {
        dictionaries.forEach { dict ->
            val testProcessor = TextProcessor(testListDictionary)
            assertEquals(listOf(), testProcessor.findMisspellings("car (12 - cat), cAb.;CaRT!CARS?"))

            assertMisspelledWords("Cars, card. cart: cats", "card", "cats")
            assertSuggestions("Cars card cart cats", 3, "cart", "car", "cars", "cars", "cat", "cab")
        }
    }

    @Test
    fun testFindMisspellingsSingleWord() {
        dictionaries.forEach { dict ->
            val testProcessor = TextProcessor(testListDictionary)
            assertEquals(listOf(), testProcessor.findMisspellings("Car"))

            assertMisspelledWords("card", "card")
            assertSuggestions("card", 3, "car", "cart", "cars")
        }
    }

    private fun assertSuggestions(text: String, maxSuggestions: Int, vararg misspellings: String) {
        dictionaries.forEach { dict ->
            val testProcessor = TextProcessor(testListDictionary)
            val res = testProcessor.findMisspellings(text, maxSuggestions)
                .flatMap { it.suggestions.map { inner -> inner.correction } }
            assertEqualIgnoreOrder(misspellings.toList(), res)
        }
    }

    private fun assertMisspelledWords(text: String, vararg misspellings: String) {
        dictionaries.forEach { dict ->
            val testProcessor = TextProcessor(testListDictionary)
            val res = testProcessor.findMisspellings(text).map { it.misspelled }
            assertContentEquals(misspellings.toList(), res)
        }
    }
}