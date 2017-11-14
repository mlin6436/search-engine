# Search App

### Environment

- Scala 2.12.4
- sbt 1.0.3
- Java 8

### Run the Test

`cd` into the project folder and execute `sbt test`

### Project Structure

.
├── README.md
├── build.sbt
└── src
    ├── main
    │   └── scala
    │       └── SearchApp.scala
    └── test
        ├── resources
        │   ├── dir
        │   │   └── test.txt
        │   └── foo
        │       ├── 1.txt
        │       ├── 2.txt
        │       ├── 3.txt
        │       └── bar
        │           └── test.txt
        └── scala
            ├── FinderSpec.scala
            └── SearcherSpec.scala
 

 In `main`, `SearchApp.scala` contains
 
 - `package object`, which is used to define tiny types to represent the data structure used in the application.
 - `SearchApp`, which is the entry point of the application, and holds files data instantiated during start up in a mutable variable.
 - `Searcher`, the main class called by `SearchApp` to perform startup (data initialisation), search (see below) and output result.
 - `Finder`, the class `Searcher` used to delegate tasks to traverse file system and load the content of the files.
 
 In `test`, there are `resources` folder containing fixtures for testing, and two specs files for testing the two main classes.
 
 ### Search Algorithm
 
 There are the following parts of the search algorithm
 
 - Finding all possible combinations of the input value
   - The input value (string) is first being split into a list of strings (determined by space).
   - Then find out all combinations of the words (formed by the words next to each other), such as "The quick brown fox" has 10 possible combinations.

 - Finding matching keywords in the content
   - First find out if there is a match on the first word from the keywords in the content, keywords is a subset (one or more words) from the input keywords.
   - If there is a match, then recursively find the existence of the second word in the content, so on and so forth.
   - If there is no match, either the search will stop and return the matching result, or return empty result. 
 
 - Finding all possible combinations of the input value (keywords) in all the loaded contents
 
 - Rank the result by using the formula: matchingKeywordsFound / totalNumberOfKeywordsFromInput
 
 - Produce the final result 