package com.voze.mtolman.com.voze.mtolman.files

import java.io.File
import java.io.FileNotFoundException

class DictionaryLoader {
    companion object {
        fun loadWordsFromResource(resource: String): List<String> {
            val resourcePath = Companion::class.java.classLoader.getResource(resource) ?: throw FileNotFoundException("Resource ${resource} does not exist")
            return resourcePath.readText().lines().filter { it.isNotBlank() }.map { it.trim() }
        }

        fun loadWordsFromFile(file: String) : List<String> {
            return File(file).readLines().filter { it.isNotBlank() }.map { it.trim() }
        }
    }
}