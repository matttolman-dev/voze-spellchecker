package com.voze.mtolman.com.voze.mtolman.text

import com.voze.mtolman.processing.results.WordResult

interface SpellcheckDictionary {
    fun misspelledNearest(word: String, maxSuggestions: Int = 10, maxCost: Int = Int.MAX_VALUE) : List<WordResult>
}