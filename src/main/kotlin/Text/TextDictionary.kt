package com.voze.mtolman.Text

import com.voze.mtolman.Text.Results.WordResult
import java.util.PriorityQueue

class TextDictionary(private val words: List<String>) {
    fun misspelledNearest(word: String, top: Int = 10) : List<WordResult> {
        val results = PriorityQueue<WordResult>(top) { l, r -> -l.distance.compareTo(r.distance) }
        for (dictWord in words) {
            val dist = distance(word, dictWord)
            if (dist == 0) {
                return listOf()
            }
            results.add(WordResult(dictWord, dist))
            if (results.size > top) {
                assert(results.peek().distance >= dist)
                results.remove()
            }
        }
        assert(results.size <= top)
        return results.reversed().toList()
    }

    companion object {
         fun distance(a: String, b: String) : Int {
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