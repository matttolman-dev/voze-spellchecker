package com.voze.mtolman.com.voze.mtolman.files

import java.io.File
import java.io.FileNotFoundException

/**
 * Loads dictionary files
 */
class DictionaryLoader {
    companion object {
        /**
         * Loads files from Java resource directories (useful for tests and CI/CD)
         */
        fun loadWordsFromResource(resource: String): List<String> {
            val resourcePath = Companion::class.java.classLoader.getResource(resource) ?: throw FileNotFoundException("Resource ${resource} does not exist")
            return resourcePath.readText().lines().filter { it.isNotBlank() }.map { it.trim().lowercase() }
        }

        /**
         * Loads files from a file path (useful for CLI and user provided paths)
         */
        fun loadWordsFromFile(file: String) : List<String> {
            return File(file).readLines().filter { it.isNotBlank() }.map { it.trim().lowercase() }
        }
    }
}