package com.voze.mtolman.com.voze.mtolman.text.dictionaries

import com.voze.mtolman.com.voze.mtolman.text.SpellcheckDictionary
import com.voze.mtolman.processing.results.WordResult
import java.util.*
import kotlin.collections.ArrayList

/**
 * Dictionary using a trie and searches the trie to calculate the levenshtein distance
 * Based on Stave Hanov's blog post about using tries to optimize levenshtein distance calculation (http://stevehanov.ca/blog/?id=114)
 * This is not fully optimized. Potential optimizations would be to change the word: String? in Node to word: Boolean and then
 * keep track of the path as we search down. Doing so would save on memory. I chose to not include a memory optimization for now
 */
class TrieDictionary(words: List<String>) : SpellcheckDictionary {
    class Node {
        var word: String? = null
        val ptrs: Array<Node?> = arrayOf(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )

        init {
            assert(ptrs.size == 26)
        }
    }

    private val head: Node = Node()

    init {
        words.forEach { insert(it) }
    }

    private fun insert(word: String) {
        val lower = word.lowercase()

        var curTrie = head
        for (letter in lower) {
            val letterIndex = (letter.code - 'a'.code)
            assert(letterIndex in 0..<26)
            curTrie = if (curTrie.ptrs[letterIndex] != null) {
                curTrie.ptrs[letterIndex]!!
            }
            else {
                val new = Node()
                curTrie.ptrs[letterIndex] = new
                new
            }
        }
        curTrie.word = word
    }

    override fun misspelledNearest(word: String, maxSuggestions: Int, maxCost: Int): List<WordResult> {
        if (contains(word) || word == "") {
            return listOf()
        }

        val levenshteinCostsPerCharacter = (0..(word.length + 1)).toList()
        val results = PriorityQueue<WordResult>(maxSuggestions + 1) { l, r -> r.distance.compareTo(l.distance) }
        val node = head
        val childrenIndices = node.ptrs.indices

        for (childIndex in childrenIndices) {
            val nodePtr = node.ptrs[childIndex] ?: continue
            search(nodePtr, 'a'.plus(childIndex), word, levenshteinCostsPerCharacter, results, maxCost, maxSuggestions)
        }
        return results.reversed().toList()
    }

    private fun search(
        node: Node,
        letter: Char,
        word: String,
        prevCostsPerCharacter: List<Int>,
        results: PriorityQueue<WordResult>,
        maxCost: Int,
        maxSuggestions: Int
    ) {
        val cols = word.length + 1
        val levenshteinCostsPerCharacter = ArrayList<Int>()
        levenshteinCostsPerCharacter.add(prevCostsPerCharacter[0] + 1)

        // Calculate the costs for every possible next letter in the tri
        for (col in 1..<cols) {
            assert(col - 1 in levenshteinCostsPerCharacter.indices)
            assert(col < prevCostsPerCharacter.size)
            assert(col - 1 in word.indices)

            val addCost = levenshteinCostsPerCharacter[col - 1] + 1
            val removeCost = prevCostsPerCharacter[col] + 1

            val replaceCost = if (word[col - 1] != letter) {
                // If we don't have a match to what we're looking for
                prevCostsPerCharacter[col - 1] + 1
            } else {
                prevCostsPerCharacter[col - 1]
            }

            levenshteinCostsPerCharacter.add(minOf(addCost, removeCost, replaceCost))
        }

        var curMaxCost = if (results.size >= maxSuggestions) {
            results.peek().distance
        } else {
            maxCost
        }

        // If we're at the end of the word and it's an optimal suggestion, then add it
        val lastRow = levenshteinCostsPerCharacter.last()
        node.word?.let {
            if (lastRow <= curMaxCost) {
                results.add(WordResult(it, lastRow))

                // Limit number of results
                if (results.size > maxSuggestions) {
                    results.remove()
                }
                curMaxCost = results.peek().distance
            }
        }

        // If we still have some small entries to search lets search them
        if (levenshteinCostsPerCharacter.min() <= curMaxCost) {
            val childrenIndices = node.ptrs.indices

            for (childIndex in childrenIndices) {
                val nodePtr = node.ptrs[childIndex] ?: continue
                search(nodePtr, 'a'.plus(childIndex), word, levenshteinCostsPerCharacter, results, curMaxCost, maxSuggestions)
            }
        }
    }


    fun contains(word: String): Boolean {
        val lower = word.lowercase()

        var curTrie = head
        for (letter in lower) {
            val letterIndex = (letter.code - 'a'.code)
            assert(letterIndex in 0..<26)
            curTrie = curTrie.ptrs[letterIndex] ?: return false
        }
        return curTrie.word != null
    }
}