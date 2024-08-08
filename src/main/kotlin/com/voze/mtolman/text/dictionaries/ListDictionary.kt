package com.voze.mtolman.com.voze.mtolman.text.dictionaries

import com.voze.mtolman.com.voze.mtolman.text.SpellcheckDictionary
import com.voze.mtolman.processing.results.CorrectionResult
import java.util.PriorityQueue

/**
 * Simple dictionary that sequentially iterates a list of strings to calculate levenshtein distance
 * Not quick, but it's easier to write and maintain.
 I use it for correctness testing when there are bugs in faster implementations (e.g. TrieDictionary)
 */
class ListDictionary(private val words: List<String>) : SpellcheckDictionary {
    /** @inheritdoc */
    override fun misspelledNearest(word: String, maxSuggestions: Int, maxDifference: Int): List<CorrectionResult> {
        var curMaxCost = maxDifference
        val results = PriorityQueue<CorrectionResult>(maxSuggestions + 1) { l, r -> r.distance.compareTo(l.distance) }
        for (dictWord in words) {
            val dist = distance(word, dictWord)
            if (dist == 0) {
                return listOf()
            }

            if (dist <= curMaxCost) {
                results.add(CorrectionResult(dictWord, dist))
                assert(results.peek().distance >= dist)
                assert(results.peek().distance <= curMaxCost)

                // If we've saturated our results then make sure we don't waste time on anything more costly than what we have
                if (results.size >= maxSuggestions) {
                    curMaxCost = results.peek().distance
                }

                // Fix over-saturation
                if (results.size > maxSuggestions) {
                    results.remove()
                }
            }
        }
        assert(results.size <= maxSuggestions)
        return results.reversed().toList()
    }

    companion object {
        /**
         * Calculates levenshtein edit distance between two words.
         * For more information on levenshtein distance, see https://en.wikipedia.org/wiki/Levenshtein_distance
         */
        fun distance(a: String, b: String): Int {
            if (a.isEmpty()) {
                return b.length;
            }

            if (b.isEmpty()) {
                return a.length
            }

            if (a[0] == b[0]) {
                return distance(a.substring(1), b.substring(1))
            }

            val addDist = distance(a.substring(1), b)
            val removeDist = distance(a, b.substring(1))
            val editDist = distance(a.substring(1), b.substring(1))

            return 1 + minOf(addDist, removeDist, editDist)
        }
    }
}