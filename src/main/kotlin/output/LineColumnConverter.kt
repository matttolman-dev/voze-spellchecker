package com.voze.mtolman.output

import com.voze.mtolman.output.results.LineColResult
import java.util.TreeMap

class LineColumnConverter(text: String) {
    private val lines: TreeMap<Int, Int> = TreeMap()

    init {
        lines[-1] = 1
        var line = 1
        for (c in text.indices) {
            if (text[c] == '\n') {
                line += 1
                lines[c] = line
            }
        }
    }

    fun linePos(textPos: Int): LineColResult {
        val lineEntry = lines.lowerEntry(textPos)
        return if (lineEntry.key <= 0) {
            LineColResult(lineEntry.value, textPos + 1)
        } else {
            LineColResult(lineEntry.value, textPos - lineEntry.key)
        }
    }
}