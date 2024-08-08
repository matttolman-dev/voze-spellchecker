package com.voze.mtolman.formatting

import com.voze.mtolman.output.CliFormatter
import com.voze.mtolman.processing.results.MisspelledTextResult
import com.voze.mtolman.processing.results.WordResult
import kotlin.test.Test
import kotlin.test.assertEquals

class CliFormatterTests {
    @Test
    fun testContext() {
        val formatter = CliFormatter("Hello, my naem is Fred.")
        val errors = listOf(MisspelledTextResult("naem", 10, listOf(WordResult("names", 3), WordResult("name", 2))))
        val expected = """
===============
Misspelling at 1:11

llo, my naem is Fred
        ^^^^

Suggestions:
  name  names
===============
"""
        assertEquals(expected, formatter.formatResults(errors))
    }

    @Test
    fun testCasing() {
        val formatter = CliFormatter("Hello,\n\nMy naem is Freid. I LVOE coding.")
        val errors = listOf(
            MisspelledTextResult("naem", 11, listOf(WordResult("names", 3), WordResult("name", 2))),
            MisspelledTextResult("Freid", 19, listOf(WordResult("fred", 1))),
            MisspelledTextResult("LVOE", 28, listOf(WordResult("love", 2))),
        )
        val expected = """
===============
Misspelling at 3:4

My naem is Frei
   ^^^^

Suggestions:
  name  names
===============

===============
Misspelling at 3:12

naem is Freid. I LVOE
        ^^^^^

Suggestions:
  Fred
===============

===============
Misspelling at 3:21

reid. I LVOE coding.
        ^^^^

Suggestions:
  LOVE
===============
"""
        val res = formatter.formatResults(errors)
        assertEquals(expected, res)
    }

    @Test
    fun testLineCols() {
        val formatter = CliFormatter("Hello,\n my\n naem\nsi Fred.")
        val errors = listOf(
            MisspelledTextResult("naem", 12, listOf(WordResult("names", 3), WordResult("name", 2))),
            MisspelledTextResult("si", 17, listOf(WordResult("is", 1), WordResult("sin", 1)))
        )
        val expected = """
===============
Misspelling at 3:2

 naem
 ^^^^

Suggestions:
  name  names
===============

===============
Misspelling at 4:1

si Fred.
^^

Suggestions:
  is  sin
===============
"""
        assertEquals(expected, formatter.formatResults(errors))
    }
}