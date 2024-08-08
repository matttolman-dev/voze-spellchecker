package com.voze.mtolman.processing.results

/**
 * Holds information about a misspelled word
 * misspelled is the text of the word that was misspelled
 * position is the original index that the word occurs in
 * suggestions is a list of spelling suggestions
 */
data class MisspelledTextResult(val misspelled: String, val position: Int, val suggestions: List<CorrectionResult>)