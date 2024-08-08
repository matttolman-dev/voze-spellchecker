package com.voze.mtolman.text

import com.voze.mtolman.com.voze.mtolman.text.dictionaries.TrieDictionary
import com.voze.mtolman.com.voze.mtolman.text.dictionaries.ListDictionary
import com.voze.mtolman.processing.results.CorrectionResult
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
                CorrectionResult("car", 1),
                CorrectionResult("bar", 1),
                CorrectionResult("far", 1),
            )

            dict.misspelledNearest("tar", 5) shouldContainExactlyInAnyOrder listOf(
                CorrectionResult("car", 1),
                CorrectionResult("bar", 1),
                CorrectionResult("far", 1),
                CorrectionResult("cat", 2),
                CorrectionResult("cab", 2),
            )

            dict.misspelledNearest("tar", 7) shouldContainExactlyInAnyOrder listOf(
                CorrectionResult("car", 1),
                CorrectionResult("bar", 1),
                CorrectionResult("far", 1),
                CorrectionResult("cat", 2),
                CorrectionResult("cab", 2),
                CorrectionResult("ode", 3),
                CorrectionResult("large", 3),
            )
        }
    }

    @Test
    fun testFindCost() {
        dictionaries.forEach { dict ->
            testListDictionary.misspelledNearest("tar", maxDifference = 2) shouldContainExactlyInAnyOrder listOf(
                    CorrectionResult("car", 1),
                    CorrectionResult("bar", 1),
                    CorrectionResult("far", 1),
                    CorrectionResult("cat", 2),
                    CorrectionResult("cab", 2),
            )

            testListDictionary.misspelledNearest("sold", maxDifference = 4) shouldContainExactlyInAnyOrder listOf(
                CorrectionResult("snail", 4),
                CorrectionResult("small", 3),
                CorrectionResult("bike", 4),
                CorrectionResult("ode", 3),
                CorrectionResult("far", 4),
                CorrectionResult("bar", 4),
                CorrectionResult("cab", 4),
                CorrectionResult("car", 4),
                CorrectionResult("cat", 4),
            )

            testListDictionary.misspelledNearest("sold", maxDifference = 3) shouldContainExactlyInAnyOrder listOf(
                CorrectionResult("small", 3),
                CorrectionResult("ode", 3),
            )

            testListDictionary.misspelledNearest("sold", maxDifference = 2) shouldContainExactlyInAnyOrder listOf()
        }
    }
}