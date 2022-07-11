package com.acervera.scalatest.fixtures

import better.files.File
import com.acervera.scalatest.fixtures.FileSystemFixtures.runTS
import org.scalatest.TestSuite

import java.time.{Instant, LocalDateTime}
import java.time.format.DateTimeFormatter
import scala.util.{Failure, Success, Try}

object FileSystemFixtures {
  val runTS: LocalDateTime = LocalDateTime.now()
}

trait FileSystemFixtures extends GenericHelpers { this: TestSuite =>

  def cleanFolderUp(
      cleanUpMode: CleanUpMode,
      tmpFolder: File,
      fn: File => Unit
  ): Unit = Try(fn(tmpFolder)) match {
    case Failure(ex) =>
      if (cleanUpMode == CLEAN_ALWAYS)
        tmpFolder.delete(swallowIOExceptions = true)
      throw ex
    case Success(_) =>
      if (cleanUpMode != KEEP_ALWAYS)
        tmpFolder.delete(swallowIOExceptions = true)
  }

  def withTemporalFolder(
      parentFolder: String = "target/testing-sandbox",
      subfolder: String = "",
      cleanUpMode: CleanUpMode = KEEP_ON_ERROR,
      timestampPattern: String = "yyyyMMddkkmmss"
  )(
      fn: File => Unit
  ): Unit = {
    def endingWith(txt: String, char: String) =
      if (!txt.endsWith(char)) txt + char else txt
    def startingWith(txt: String, char: String) =
      if (!txt.startsWith(char)) char + txt else txt

    val slashedSubfolder = startingWith(endingWith(subfolder, "/"), "/")

    val tmpFolder = File(
      s"$parentFolder/$testPath$slashedSubfolder${DateTimeFormatter.ofPattern(timestampPattern).format(runTS)}"
    )

    cleanFolderUp(cleanUpMode, tmpFolder, fn)
  }
}
