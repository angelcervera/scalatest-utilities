package com.acervera.scalatest.fixtures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import java.sql.DriverManager

class JdbcFixturesSpecs
    extends AnyWordSpecLike
    with Matchers
    with JdbcFixtures {

  "JdbcFixtures" should {
    "withH2Connection" in withH2Connection(
      setUpScript = """
          | CREATE TABLE PERSONS (
          |    id int,
          |    name varchar(255)
          |);
          |""".stripMargin,
      cleanUpScript = """
          | DROP TABLE PERSONS
          |""".stripMargin
    ) { con =>
      con
        .prepareStatement("""
          | INSERT INTO PERSONS (id, name) VALUES (2, 'name_2'), (2, 'name_2')
          |""".stripMargin)
        .execute()

      val result =
        con.prepareStatement("SELECT COUNT(*) AS C FROM PERSONS").executeQuery()

      result.next()
      result.getInt("C") shouldBe 2
    }

    "openNewConnection" in withH2Connection(
      setUpScript = """
                      | CREATE TABLE PERSONS (
                      |    id int,
                      |    name varchar(255)
                      |);
                      |""".stripMargin,
      cleanUpScript = """
                        | DROP TABLE PERSONS
                        |""".stripMargin
    ) { con =>

      val con2 = DriverManager.getConnection(con.getMetaData.getURL)
      try {
        con2
          .prepareStatement(
            """
              | INSERT INTO PERSONS (id, name) VALUES (2, 'name_2'), (2, 'name_2')
              |""".stripMargin)
          .execute()

        val result =
          con2
            .prepareStatement("SELECT COUNT(*) AS C FROM PERSONS")
            .executeQuery()

        result.next()
        result.getInt("C") shouldBe 2

      } finally {
        con2.close()
      }
    }

  }
}
