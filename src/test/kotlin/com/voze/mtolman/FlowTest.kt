package com.voze.mtolman

import com.voze.mtolman.com.voze.mtolman.files.DictionaryLoader
import com.voze.mtolman.com.voze.mtolman.files.TextLoader
import com.voze.mtolman.com.voze.mtolman.text.TextProcessor
import com.voze.mtolman.com.voze.mtolman.text.dictionaries.TrieDictionary
import com.voze.mtolman.output.CliFormatter
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test

class FlowTest {
    @Test
    fun testSmall() {
        val dictionary = TrieDictionary(DictionaryLoader.loadWordsFromResource("dictionary.txt"))
        val processor = TextProcessor(dictionary)
        val text = TextLoader.loadFromResource("sample_file.txt")
        val formatter = CliFormatter(text)
        val output = formatter.formatResults(processor.findMisspellings(text))
        output shouldContain  "Misspelling at 1:9\n" +
                "\n" +
                "This is a sample \n" +
                "        ^\n" +
                "\n" +
                "Suggestions:\n" +
                "  ad"
        output shouldContain "Misspelling at 2:1\n" +
                "\n" +
                "I have a \n" +
                "^\n" +
                "\n" +
                "Suggestions:\n" +
                "  ID  BI  AI"
        output shouldContain "Misspelling at 2:8"
        output shouldContain "Misspelling at 2:10"
        output shouldContain "Misspelling at 2:14\n" +
                "\n" +
                "e a fwe Spellling Errrors\n" +
                "        ^^^^^^^^^\n" +
                "\n" +
                "Suggestions:\n" +
                "  Spelling  Snellin"
        output shouldContain "Misspelling at 2:24"
        output shouldContain "Misspelling at 3:10"
        output shouldContain "Misspelling at 3:30"
    }

    @Test
    fun testChatGPT() {
        val dictionary = TrieDictionary(DictionaryLoader.loadWordsFromResource("dictionary.txt"))
        val processor = TextProcessor(dictionary)
        val text = TextLoader.loadFromResource("chatgpt_sample.txt")
        val formatter = CliFormatter(text)
        val output = formatter.formatResults(processor.findMisspellings(text))
        output shouldContain "Misspelling at 1:8"
        output shouldContain "Misspelling at 1:27"
        output shouldContain "Misspelling at 1:91"
        output shouldContain "Misspelling at 1:106"
        output shouldContain "Misspelling at 1:140"
        output shouldContain "Misspelling at 1:160"
        output shouldContain "Misspelling at 1:187"
        output shouldContain "Misspelling at 1:232"
        output shouldContain "Misspelling at 1:260"
        output shouldContain "Misspelling at 3:4"
        output shouldContain "Misspelling at 3:26"
        output shouldContain "Misspelling at 3:50"
        output shouldContain "Misspelling at 3:74"
        output shouldContain "Misspelling at 3:99"
        output shouldContain "Misspelling at 3:126"
        output shouldContain "Misspelling at 3:145"
        output shouldContain "Misspelling at 7:275"
        output shouldContain "Misspelling at 15:89"
    }
}