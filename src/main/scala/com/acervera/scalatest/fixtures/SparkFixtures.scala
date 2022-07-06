package com.acervera.scalatest.fixtures

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.scalatest.TestSuite

import scala.language.implicitConversions

trait SparkFixtures extends GenericHelpers { this: TestSuite =>

  def withDeltaSession(
      defaultConfig: SparkConf = new SparkConf(),
      master: String = "local[*]"
  )(fn: SparkSession => Unit): Unit = {
    val confWithDelta = defaultConfig
      .set(
        "spark.sql.extensions",
        "io.delta.sql.DeltaSparkSessionExtension"
      )
      .set(
        "spark.sql.catalog.spark_catalog",
        "org.apache.spark.sql.delta.catalog.DeltaCatalog"
      )

    withSparkSession(confWithDelta, master)(fn)
  }

  def withSparkSession(
      defaultConfig: SparkConf = new SparkConf(),
      master: String = "local[*]"
  )(
      fn: SparkSession => Unit
  ): Unit = {

    val spark: SparkSession = SparkSession
      .builder()
      .config(defaultConfig)
      .appName(s"Test $suiteId")
      .master(master)
      .getOrCreate()

    // TODO: Warehouse cleanup
    try {
      fn(spark)
    } finally {
      spark.close()
    }
  }
}
