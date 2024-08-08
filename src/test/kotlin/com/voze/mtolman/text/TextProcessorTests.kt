package com.voze.mtolman.text

import com.voze.mtolman.assertEqualIgnoreOrder
import com.voze.mtolman.com.voze.mtolman.text.SpellcheckDictionary
import com.voze.mtolman.com.voze.mtolman.text.dictionaries.TrieDictionary
import com.voze.mtolman.com.voze.mtolman.text.dictionaries.ListDictionary
import com.voze.mtolman.com.voze.mtolman.text.TextProcessor
import io.kotest.matchers.shouldBe
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
    fun testFindMisspellingsSingleLetters() {
        dictionaries.forEach { dict ->
            val testProcessor = TextProcessor(dict)
            val res = testProcessor.findMisspellings("I am a sample i c").map { it.misspelled }
            val positions = testProcessor.findMisspellings("I am a sample i c").map { it.position }
            res shouldBe listOf("I", "am", "a", "sample", "i", "c")
            positions shouldBe listOf(0, 2, 5, 7, 14, 16)

            val res2 = testProcessor.findMisspellings("I\nam\na\nsample\ni\nc").map { it.misspelled }
            val positions2 = testProcessor.findMisspellings("I\nam\na\nsample\ni\nc").map { it.position }
            res2 shouldBe listOf("I", "am", "a", "sample", "i", "c")
            positions2 shouldBe listOf(0, 2, 5, 7, 14, 16)
        }
    }

    @Test
    fun testFindMisspellingsMultipleWords() {
        dictionaries.forEach { dict ->
            val testProcessor = TextProcessor(dict)
            assertEquals(listOf(), testProcessor.findMisspellings("car cat cAb CaRT CARS"))

            assertMisspelledWords(dict, "Cars card cart cats", "card", "cats")
            assertSuggestions(dict, "Cars card cart cats", 1, "cart", "car", "cars", "cars", "cat")
        }
    }

    @Test
    fun testFindMisspellingsMultipleWordsPunctuation() {
        dictionaries.forEach { dict ->
            val testProcessor = TextProcessor(dict)
            assertEquals(listOf(), testProcessor.findMisspellings("car (12 - cat), cAb.;CaRT!CARS?"))

            assertMisspelledWords(dict, "Cars, card. cart: cats", "card", "cats")
            assertSuggestions(dict, "Cars card cart cats", 2, "car", "cart", "cars", "cat", "cab", "cat", "cart", "cars", "car", "cab")
        }
    }

    @Test
    fun testFindMisspellingsSingleWord() {
        dictionaries.forEach { dict ->
            val testProcessor = TextProcessor(dict)
            assertEquals(listOf(), testProcessor.findMisspellings("Car"))

            assertMisspelledWords(dict, "card", "card")
            assertSuggestions(dict, "card", 1, "car", "cart", "cars")
        }
    }

    private fun assertSuggestions(dict: SpellcheckDictionary, text: String, maxCost: Int, vararg misspellings: String) {
        val testProcessor = TextProcessor(dict)
        val res = testProcessor.findMisspellings(text, maxDifference = maxCost)
            .flatMap { it.suggestions.map { inner -> inner.correction } }
        assertEqualIgnoreOrder(misspellings.toList(), res)
    }

    private fun assertMisspelledWords(dict: SpellcheckDictionary, text: String, vararg misspellings: String) {
        val testProcessor = TextProcessor(dict)
        val res = testProcessor.findMisspellings(text).map { it.misspelled }
        assertContentEquals(misspellings.toList(), res)
    }
}