# Spellchecker

Problem: https://github.com/Voze-HQ/coding-exercises/tree/master/spell-checker

This spellchecker was written in Kotlin. To run it, have a modern version of the JDK (I used JDK 21) and Maven 3 (I used 3.9.6).
Run `mvn clean install` to run tests and build the program. The output jar will then be in `target/spellchecker-1.0-SNAPSHOT.jar`.

The executable takes two CLI args. The first is the path to the dictionary file which is in the format of a single word on each line.
The second is the path to the file to check.

The dictionary provided by the problem is available at `src/test/resources/dictionary.txt`.
Sample input files are provided at `src/test/resources/chatgpt_sample.txt`, `src/test/resources/good_file.txt`, and `src/test/resources/sample_file.txt`.

Below is an example command to run the spellchecker (once built with `mvn install`):

```bash
java -jar target/spellchecker-1.0-SNAPSHOT.jar src/test/resources/dictionary.txt src/test/resources/good_file.txt
```
