package com.voze.mtolman.com.voze.mtolman.text

import com.voze.mtolman.processing.results.MisspelledTextResult

class TextProcessor(private val listDictionary: SpellcheckDictionary) {
    fun findMisspellings(inputText: String, maxSuggestions: Int = 10, maxCost: Int = Int.MAX_VALUE) : List<MisspelledTextResult> {
        var wordStart = 0
        var inWord = false
        val results = ArrayList<MisspelledTextResult>(inputText.length / 16 + 8)

        for (a in 0.. inputText.length) {
            if (a == inputText.length || !inputText[a].isLetter()) {
                if (inWord) {
                    inWord = false
                    val word = inputText.substring(wordStart, a)
                    val spellingResults = listDictionary.misspelledNearest(word.lowercase(), maxSuggestions, maxCost)
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