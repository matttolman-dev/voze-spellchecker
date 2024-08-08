package com.voze.mtolman

import com.voze.mtolman.com.voze.mtolman.files.DictionaryLoader
import com.voze.mtolman.com.voze.mtolman.files.TextLoader
import com.voze.mtolman.com.voze.mtolman.text.TextProcessor
import com.voze.mtolman.com.voze.mtolman.text.dictionaries.TrieDictionary
import com.voze.mtolman.output.CliFormatter
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.size != 2) {
        println("Usage:\n\texecutable <dictionary_file> <check_file>")
        exitProcess(2)
    }

    val dictFilePath = args[0]
    val textFilePath = args[1]

    val dictionary = TrieDictionary(DictionaryLoader.loadWordsFromFile(dictFilePath))
    val processor = TextProcessor(dictionary)
    val text = TextLoader.loadFromFile(textFilePath)
    val formatter = CliFormatter(text)
    val results = processor.findMisspellings(text)
    val output = formatter.formatResults(results)

    println(output)

    // Set a non-zero exit code if there was a misspelling
    // Makes it easier to check if there's a misspelling on bash (e.g. in CI/CD)
    val exitCode = if (results.isEmpty()) {
        0
    }
    else {
        1
    }

    exitProcess(exitCode)
}