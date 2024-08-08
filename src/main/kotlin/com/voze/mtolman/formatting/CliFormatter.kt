package com.voze.mtolman.output

import com.voze.mtolman.processing.results.ContextResult
import com.voze.mtolman.processing.results.MisspelledTextResult

/**
 * Handles formatting misspelling output for CLI interfaces
 * It takes the original text since it will preprocess the line/column information for the text
 */
class CliFormatter(private val originalText: String) {
    private val lineColTracker = LineColumnConverter(originalText)

    /** Case information used for handling Proper Nouns and ACRONYMS */
    enum class CaseType {
        UPPER, TITLE, OTHER
    }

    /**
     * Formats results for CLI output
     * @param misspelled The misspelled text results
     * @param contextSurrounding The maximum before and after characters to include for the context
     */
    fun formatResults(misspelled: List<MisspelledTextResult>, contextSurrounding: Int = 8): String {
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

            if (misspelling.suggestions.isEmpty()) {
                result.append("\n\nNo suggestions found")
            } else {
                result
                    .append("\n\nSuggestions:\n  ")
                    .append(misspelling.suggestions.sortedBy { it.distance }
                        .joinToString("  ") { mirrorCase(case, it.correction) })
            }
            result.append("\n===============\n")
        }

        return result.toString()
    }

    /**
     * Pulls context from the original text for a misspelling
     * Will drop newline characters
     */
    private fun pullContext(
        originalText: String,
        misspelling: MisspelledTextResult,
        contextSurrounding: Int
    ): ContextResult {
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

    /** Detects casing patterns to be Title, UPPER, or other */
    private fun detectCaseType(originalWord: String): CaseType {
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

    /** Tries to mirror casing of another word for better Proper Noun and ACRONYM display for suggestions */
    private fun mirrorCase(case: CaseType, suggestedWord: String): String {
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