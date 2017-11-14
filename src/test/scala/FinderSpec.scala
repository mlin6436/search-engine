import java.io.File

import org.scalatest.FunSpec
import org.scalatest.Matchers._

import scala.io.Source

class FinderSpec extends FunSpec {

  describe("Finder") {
    val finder = new Finder

    it("should return all the files given a directory") {
      val result = finder.getFiles(new File(getClass.getResource("/dir").getPath))

      result.head.getAbsolutePath should include("test.txt")
    }

    it("should recursively find all files given a directory") {
      val result = finder.getFiles(new File(getClass.getResource("/foo").getPath))

      result.head.getAbsolutePath should include("1.txt")
    }

    it("should return empty list if the given directory does not exist") {
      val result = finder.getFiles(new File("doesnotexist"))

      result.size shouldBe 0
    }

    it("should return empty list if the given directory is not a directory") {
      val result = finder.getFiles(new File(getClass.getResource("/dir/test.txt").getPath))

      result.size shouldBe 0
    }

    it("should return the content of a file") {
      val testFile = new File(getClass.getResource("/dir/test.txt").getPath)
      val result = finder.getFileContent(testFile)

      result shouldBe Source.fromFile(testFile).mkString
    }
  }
}
