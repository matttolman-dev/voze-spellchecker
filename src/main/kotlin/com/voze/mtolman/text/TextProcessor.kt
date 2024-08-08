package com.voze.mtolman.com.voze.mtolman.text

import com.voze.mtolman.processing.results.MisspelledTextResult

/**
 * Handles processing text for finding misspellings
 * Takes an input dictionary
 */
class TextProcessor(private val spellcheckDictionary: SpellcheckDictionary) {

    /**
     * Returns a list of all misspelled words with their index in the input text and a list of suggestions
     * All suggestions are returned in lowercase
     * It will ignore the case of the input text and all non-alphabetic ASCII characters
     * @param inputText The text to search for misspellings
     * @param maxSuggestions The maximum number of suggestions to return. It will return the "best" suggestions
     * @param maxDifference The maximum difference between the input word and the output suggestions. If not provided, it will not filter the output suggestions
     */
    fun findMisspellings(inputText: String, maxSuggestions: Int = 10, maxDifference: Int = Int.MAX_VALUE) : List<MisspelledTextResult> {
        var wordStart = 0
        var inWord = false
        val results = ArrayList<MisspelledTextResult>(inputText.length / 16 + 8)

        for (a in 0.. inputText.length) {
            if (a == inputText.length || !inputText[a].isLetter()) {
                if (inWord) {
                    inWord = false
                    val word = inputText.substring(wordStart, a)
                    val spellingResults = spellcheckDictionary.misspelledNearest(word.lowercase(), maxSuggestions, maxDifference)
                    if (spellingResults.isNotEmpty()) {
                        results.add(MisspelledTextResult(word, wordStart, spellingResults))
                    }
                }
            }
            else if (!inWord) {
                wordStart = a
                inWord = true
            }
        }
        return results
    }
}