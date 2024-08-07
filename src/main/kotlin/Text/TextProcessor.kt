package com.voze.mtolman.Text

import com.voze.mtolman.Text.Results.MisspelledTextResult

class TextProcessor(private val textDictionary: TextDictionary) {
    fun findMisspellings(inputText: String, maxSuggestions: Int = 10) : List<MisspelledTextResult> {
        var wordStart = 0
        var inWord = false
        val results = ArrayList<MisspelledTextResult>(inputText.length / 16 + 8)

        for (a in 0.. inputText.length) {
            if (a == inputText.length || !inputText[a].isLetter()) {
                if (inWord) {
                    inWord = false
                    val word = inputText.substring(wordStart, a)
                    val spellingResults = textDictionary.misspelledNearest(word.lowercase(), maxSuggestions)
                    if (spellingResults.isNotEmpty()) {
                        results.add(MisspelledTextResult(word, a, spellingResults))
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