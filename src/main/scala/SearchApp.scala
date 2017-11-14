import java.io.File

import Search._

import scala.io.Source

package object Search {

  type Ranking = Int
  type FileName = String
  type Content = String
  type Keywords = List[Keyword]
  type Keyword = String
  type FileContents = List[(FileName, Content)]
}

object SearchApp extends App {

  var fileContents: FileContents = Nil
}

class Searcher(finder: Finder) {

  def startup(dir: File): Unit = {
    val files = finder.getFiles(dir)
    SearchApp.fileContents = files.map(file => (file.getName, finder.getFileContent(file)))
  }

  def search(input: String): String = {
    val searchResult = getKeywordsCombinations(input).flatMap { keywords =>
      findMatchingKeywordsInAllFiles(keywords, SearchApp.fileContents)
    }
    val searchResultPercentage = searchResult.map { case (fileName, matchingKeywords, keywords) =>
      fileName -> matchingKeywords.size * 100.0f / keywords.size
    }
    val groupedByFile = searchResultPercentage.groupBy { case (fileName, _) => fileName }
    groupedByFile.map {
      case (fileName, searchResultPercentage) => fileName + " : " + resultMessage(searchResultPercentage.maxBy(_._2)._2)
    }.mkString("\n")
  }

  def resultMessage(percentage: Float): String =
    if (percentage == 0.0)
      "no matches found"
    else
      percentage + "%"


  def getKeywordsCombinations(searchInput: String): List[Keywords] =
    searchInput.split(" ").toList.inits.flatMap(_.tails.toList.init).toList

  def findMatchingKeywordsInAllFiles(keywords: Keywords, fileContents: FileContents): List[(FileName, Keywords, Keywords)] =
    fileContents.map { case (fileName: FileName, content: Content) =>
      (fileName, findMatchingKeywords(keywords, content), keywords)
    }

  def findMatchingKeywords(keywords: Keywords, content: Content): Keywords =
    if (keywords.nonEmpty && content.indexOf(keywords.head) != -1)
      List(keywords.head) ++ findMatchingKeywords(keywords.tail, content.substring(content.indexOf(keywords.head) + 1))
    else
      Nil
}

class Finder {

  def getFiles(dir: File): List[File] =
    if (dir.exists && dir.isDirectory) {
      val files = dir.listFiles.toList
      files.filter(_.isFile) ++ files.filter(_.isDirectory).flatMap(getFiles)
    }
    else
      Nil

  def getFileContent(file: File): Content = Source.fromFile(file).mkString
}