package com.voze.mtolman.processing.results

/**
 * Container to hold word context information
 * preWord tracks how many characters appear before the original word
 * context is a string surrounding the word
 */
data class ContextResult(val preWord: Int, val context: String)
