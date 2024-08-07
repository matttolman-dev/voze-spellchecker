package com.voze.mtolman.processing.results

data class MisspelledTextResult(val misspelled: String, val position: Int, val suggestions: List<WordResult>)