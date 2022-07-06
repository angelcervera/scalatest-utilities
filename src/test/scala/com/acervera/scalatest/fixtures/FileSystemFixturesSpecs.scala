package com.acervera.scalatest.fixtures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class FileSystemFixturesSpecs
    extends AnyWordSpecLike
    with FileSystemFixtures
    with Matchers {

  "FileSystemFixtures" should {
    "withTemporalFolder" when {
      val expected =
        "target/testing-sandbox/com/acervera/scalatest/fixtures/FileSystemFixturesSpecs/xxxx/"
      "there is a subfolder without slashes" in withTemporalFolder(subfolder =
        "xxxx"
      ) { f => f.toString() should include(expected) }
      "there is a subfolder with slashes" in withTemporalFolder(subfolder =
        "/xxxx/"
      ) { f => f.toString() should include(expected) }
    }
  }
}
