import java.io.File

import org.scalatest.FunSpec
import org.scalatest.Matchers._

class SearcherSpec extends FunSpec {

  describe("Search") {
    val finder = new Finder
    val searcher = new Searcher(finder)

    searcher.startup(new File(getClass.getResource("/foo").getPath))

    it("should return matching keywords if they can be found in the content") {
      val result = searcher.findMatchingKeywords(List("Lorem", "ipsum"), SearchApp.fileContents.head._2)

      result shouldBe List("Lorem", "ipsum")
    }

    it("should return empty if no match can be found in the content") {
      val result = searcher.findMatchingKeywords(List("123", "456", "789"), SearchApp.fileContents.head._2)

      result shouldBe Nil
    }

    it("should return search result in all files") {
      val result = searcher.findMatchingKeywordsInAllFiles(List("Lorem", "ipsum"), SearchApp.fileContents)

      result shouldBe List(
        ("1.txt", List("Lorem", "ipsum"), List("Lorem", "ipsum")),
        ("2.txt", List(), List("Lorem", "ipsum")),
        ("3.txt", List(), List("Lorem", "ipsum")),
        ("test.txt", List(), List("Lorem", "ipsum")))
    }

    it("should return all combinations of the input keywords") {
      val result = searcher.getKeywordsCombinations("The quick brown fox")

      result shouldBe List(
        List("The", "quick", "brown", "fox"),
        List("quick", "brown", "fox"),
        List("brown", "fox"),
        List("fox"),
        List("The", "quick", "brown"),
        List("quick", "brown"),
        List("brown"),
        List("The", "quick"),
        List("quick"),
        List("The")
      )
    }

    it("should return no matches found when there are no matching keywords") {
      val result = searcher.search("Lorem ipsum")

      result shouldBe "3.txt : no matches found\n1.txt : 100.0%\ntest.txt : no matches found\n2.txt : no matches found"
    }
  }
}
