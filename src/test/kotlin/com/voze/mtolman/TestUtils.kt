package com.voze.mtolman

import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.assertTrue

fun <T> assertEqualIgnoreOrder(expected: List<T>, actual: List<T>) {
    val message = "Expected ${expected.joinToString(",")} to equal ${actual.joinToString(",")} (ignore order)"
    assertEquals(expected.size, actual.size, message)
    assertTrue(expected.containsAll(actual), message)
    assertTrue(actual.containsAll(expected), message)
}
