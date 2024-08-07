package com.voze.mtolman.text

import com.voze.mtolman.processing.TextDictionary
import kotlin.test.Test
import kotlin.test.assertEquals

class DistanceTests {
    @Test
    fun wikipediaExamples() {
        assertEquals(1, TextDictionary.distance("kitten", "sitten"))
        assertEquals(1, TextDictionary.distance("sitten", "sittin"))
        assertEquals(1, TextDictionary.distance("sittin", "sitting"))
        assertEquals(1, TextDictionary.distance("uninformed", "uniformed"))
    }

    @Test
    fun moreComplexExamples() {
        assertEquals(2, TextDictionary.distance("digs", "dog"))
        assertEquals(3, TextDictionary.distance("iter", "bitten"))
        assertEquals(3, TextDictionary.distance("cross", "drove"))
        assertEquals(4, TextDictionary.distance("mix", "boxer"))
    }

    @Test
    fun edgeCases() {
        assertEquals(0, TextDictionary.distance("", ""))
        assertEquals(3, TextDictionary.distance("", "abc"))
        assertEquals(3, TextDictionary.distance("abc", ""))
        assertEquals(0, TextDictionary.distance("abc", "abc"))
    }
}