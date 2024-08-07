package com.voze.mtolman.Text.Results

data class MisspelledTextResult(val misspelled: String, val position: Int, val suggestions: List<WordResult>)