package com.acervera.scalatest.fixtures

import better.files.File
import org.h2.tools.Server
import org.scalatest.TestSuite

import java.sql.{Connection, DriverManager}
import scala.util.{Failure, Success, Try, Using}

trait JdbcFixtures extends FileSystemFixtures { this: TestSuite =>

  def withJdbcConnection(
      url: String,
      setUpScript: String = "",
      cleanUpScript: String = "",
      cleanUpMode: CleanUpMode = KEEP_ON_ERROR
  )(test: Connection => Unit): Unit = {
    Using(DriverManager.getConnection(url)) { con =>
      if (setUpScript.nonEmpty) {
        con.prepareStatement(setUpScript).execute()
      }

      Try {
        test(con)
      } match {
        case Failure(ex) =>
          if (cleanUpMode == CLEAN_ALWAYS && cleanUpScript.nonEmpty)
            con.prepareStatement(cleanUpScript).execute()
          throw ex
        case Success(_) =>
          if (cleanUpMode != KEEP_ALWAYS && cleanUpScript.nonEmpty)
            con.prepareStatement(cleanUpScript).execute()
      }
    } match {
      case Failure(ex) => throw ex
      case Success(_)  =>
    }
  }

  def withH2Connection(
      setUpScript: String = "",
      cleanUpScript: String = "",
      cleanUpMode: CleanUpMode = KEEP_ON_ERROR
  )(test: Connection => Unit): Unit =
    withTemporalFolder(cleanUpMode = cleanUpMode) { tmpFolder =>
      val folder = File(s"$tmpFolder/h2-dbs")
      folder.createDirectories()

      val server =
        Server.createTcpServer(
          "-tcpAllowOthers",
          "-baseDir",
          folder.toString(),
          "-ifNotExists"
        )

      try {
        server.start()
        withJdbcConnection(
          s"jdbc:h2:tcp://localhost:${server.getPort}/database",
          setUpScript,
          cleanUpScript
        )(test)
      } finally {
        server.shutdown()
      }

    }

}
