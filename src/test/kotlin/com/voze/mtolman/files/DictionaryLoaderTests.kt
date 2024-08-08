package com.voze.mtolman.files

import com.voze.mtolman.com.voze.mtolman.files.DictionaryLoader
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class DictionaryLoaderTests {
    @Test
    fun testLoadDictionaryFile() {
        val res = DictionaryLoader.loadWordsFromResource("dictionary.txt")
        res.size shouldBe 172823
    }
}