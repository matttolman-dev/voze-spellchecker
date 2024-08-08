package com.voze.mtolman.output

import com.voze.mtolman.processing.results.ContextResult
import com.voze.mtolman.processing.results.MisspelledTextResult

class CliFormatter(private val originalText: String) {
    private val lineColTracker = LineColumnConverter(originalText)
    enum class CaseType {
        UPPER, TITLE, OTHER
    }

    fun formatResults(misspelled: List<MisspelledTextResult>, contextSurrounding: Int = 8) : String {
        if (misspelled.isEmpty()) {
            return "No words misspelled, great job!";
        }

        val result = StringBuilder()

        for (misspelling in misspelled) {
            val context = pullContext(originalText, misspelling, contextSurrounding)
            val pre = if (context.preWord >= 1) {
                " ".repeat(context.preWord)
            } else {
                ""
            }
            val lineCol = lineColTracker.linePos(misspelling.position)
            val case = detectCaseType(misspelling.misspelled)
            result
                .append("\n===============\nMisspelling at ")
                .append(lineCol.line)
                .append(":")
                .append(lineCol.col)
                .append("\n\n")
                .append(context.context)
                .append('\n')
                .append(pre)
                .append("^".repeat(misspelling.misspelled.length))
                .append("\n\nSuggestions:\n  ")
                .append(misspelling.suggestions.sortedBy { it.distance }
                    .joinToString("  ") { mirrorCase(case, it.correction) })
                .append("\n===============\n")
        }

        return result.toString()
    }

    private fun pullContext(originalText: String, misspelling: MisspelledTextResult, contextSurrounding: Int) : ContextResult {
        val preIndex = 0.coerceAtLeast(misspelling.position - contextSurrounding)
        val postIndex =
            (misspelling.position + misspelling.misspelled.length + contextSurrounding).coerceAtMost(originalText.length)
        var substring = originalText.substring(preIndex, postIndex)

        var preCount = misspelling.position - preIndex

        var newlineIndex = substring.indexOf('\n')
        while (newlineIndex in 0..preIndex) {
            substring = substring.substring(newlineIndex + 1)
            preCount -= newlineIndex + 1
            newlineIndex = substring.indexOf('\n')
        }

        if (newlineIndex >= 0) {
            substring = substring.substring(0, newlineIndex)
        }

        return ContextResult(preCount, substring)
    }

    private fun detectCaseType(originalWord: String) : CaseType {
        if (originalWord.isBlank()) {
            return CaseType.OTHER
        }

        if (originalWord.uppercase() == originalWord) {
            return CaseType.UPPER
        }

        if (originalWord[0].isUpperCase()) {
            return CaseType.TITLE
        }
        return CaseType.OTHER
    }

    private fun mirrorCase(case: CaseType, suggestedWord: String) : String {
        if (suggestedWord.isBlank()) {
            return suggestedWord
        }

        if (case == CaseType.TITLE) {
            return suggestedWord[0].uppercase() + suggestedWord.substring(1)
        }

        if (case == CaseType.UPPER) {
            return suggestedWord.uppercase()
        }

        return suggestedWord
    }
}