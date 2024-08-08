package com.voze.mtolman.com.voze.mtolman.text

import com.voze.mtolman.processing.results.CorrectionResult

/**
 * Interface for a spell checking dictionary
 */
interface SpellcheckDictionary {
    /**
     * Returns a list of the nearest words for a misspelled word
     * If the word is correctly spelled it will return the word
     * All suggestions are returned in lowercase
     * It will ignore the case of the input word
     * @param word The word to search for
     * @param maxSuggestions The maximum number of suggestions to return. It will return the "best" suggestions
     * @param maxDifference The maximum difference between the input word and the output suggestions. If not provided, it will not filter the output suggestions
     */
    fun misspelledNearest(word: String, maxSuggestions: Int = 10, maxDifference: Int = Int.MAX_VALUE) : List<CorrectionResult>
}