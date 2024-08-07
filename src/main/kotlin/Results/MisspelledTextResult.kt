package com.voze.mtolman.Results

data class MisspelledTextResult(val misspelled: String, val position: Int, val suggestions: List<WordResult>)