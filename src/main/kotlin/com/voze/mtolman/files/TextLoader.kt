package com.voze.mtolman.com.voze.mtolman.files

import java.io.File
import java.io.FileNotFoundException

/**
 * Loads text files
 */
class TextLoader {
    companion object {
        /**
         * Loads from resources
         */
        fun loadFromResource(resource: String): String {
            val resourcePath = Companion::class.java.classLoader.getResource(resource) ?: throw FileNotFoundException("Resource ${resource} does not exist")
            return resourcePath.readText()
        }

        /**
         * Loads from disk
         */
        fun loadFromFile(file: String) : String {
            return File(file).readText()
        }
    }
}