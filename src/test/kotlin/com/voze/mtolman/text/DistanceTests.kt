package com.voze.mtolman.text

import com.voze.mtolman.com.voze.mtolman.text.dictionaries.ListDictionary
import kotlin.test.Test
import kotlin.test.assertEquals

class DistanceTests {
    @Test
    fun wikipediaExamples() {
        assertEquals(1, ListDictionary.distance("kitten", "sitten"))
        assertEquals(1, ListDictionary.distance("sitten", "sittin"))
        assertEquals(1, ListDictionary.distance("sittin", "sitting"))
        assertEquals(1, ListDictionary.distance("uninformed", "uniformed"))
    }

    @Test
    fun moreComplexExamples() {
        assertEquals(2, ListDictionary.distance("digs", "dog"))
        assertEquals(3, ListDictionary.distance("iter", "bitten"))
        assertEquals(3, ListDictionary.distance("cross", "drove"))
        assertEquals(4, ListDictionary.distance("mix", "boxer"))
    }

    @Test
    fun edgeCases() {
        assertEquals(0, ListDictionary.distance("", ""))
        assertEquals(3, ListDictionary.distance("", "abc"))
        assertEquals(3, ListDictionary.distance("abc", ""))
        assertEquals(0, ListDictionary.distance("abc", "abc"))
    }
}