package com.voze.mtolman.text

import com.voze.mtolman.com.voze.mtolman.text.dictionaries.TrieDictionary
import com.voze.mtolman.com.voze.mtolman.text.dictionaries.ListDictionary
import com.voze.mtolman.processing.results.WordResult
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class ListDictionaryTests {
    companion object {
        val testListDictionary = ListDictionary(listOf("cat", "car", "cab", "bar", "far", "ode", "bike", "trick", "large", "small", "snail"))
        val testTrieDictionary = TrieDictionary(listOf("cat", "car", "cab", "bar", "far", "ode", "bike", "trick", "large", "small", "snail"))
        val dictionaries = listOf(testListDictionary, testTrieDictionary)
    }

    @Test
    fun testFindExact() {
        dictionaries.forEach { dict ->
            dict.misspelledNearest("car") shouldBe listOf()
            dict.misspelledNearest("snail") shouldBe listOf()
        }
    }

    @Test
    fun testFindClosest() {
        dictionaries.forEach { dict ->
            dict.misspelledNearest("tar", 3) shouldContainExactlyInAnyOrder listOf(
                WordResult("car", 1),
                WordResult("bar", 1),
                WordResult("far", 1),
            )

            dict.misspelledNearest("tar", 5) shouldContainExactlyInAnyOrder listOf(
                WordResult("car", 1),
                WordResult("bar", 1),
                WordResult("far", 1),
                WordResult("cat", 2),
                WordResult("cab", 2),
            )

            dict.misspelledNearest("tar", 7) shouldContainExactlyInAnyOrder listOf(
                WordResult("car", 1),
                WordResult("bar", 1),
                WordResult("far", 1),
                WordResult("cat", 2),
                WordResult("cab", 2),
                WordResult("ode", 3),
                WordResult("large", 3),
            )
        }
    }

    @Test
    fun testFindCost() {
        dictionaries.forEach { dict ->
            testListDictionary.misspelledNearest("tar", maxCost = 2) shouldContainExactlyInAnyOrder listOf(
                    WordResult("car", 1),
                    WordResult("bar", 1),
                    WordResult("far", 1),
                    WordResult("cat", 2),
                    WordResult("cab", 2),
            )

            testListDictionary.misspelledNearest("sold", maxCost = 4) shouldContainExactlyInAnyOrder listOf(
                WordResult("snail", 4),
                WordResult("small", 3),
                WordResult("bike", 4),
                WordResult("ode", 3),
                WordResult("far", 4),
                WordResult("bar", 4),
                WordResult("cab", 4),
                WordResult("car", 4),
                WordResult("cat", 4),
            )

            testListDictionary.misspelledNearest("sold", maxCost = 3) shouldContainExactlyInAnyOrder listOf(
                WordResult("small", 3),
                WordResult("ode", 3),
            )

            testListDictionary.misspelledNearest("sold", maxCost = 2) shouldContainExactlyInAnyOrder listOf()
        }
    }
}