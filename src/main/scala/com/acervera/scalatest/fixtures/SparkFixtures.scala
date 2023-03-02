package com.acervera.scalatest.fixtures

import better.files.File
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.SparkSession.Builder
import org.scalatest.TestSuite

import scala.language.implicitConversions

trait SparkFixtures extends GenericHelpers with FileSystemFixtures {
  this: TestSuite =>

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

    withSparkSession(None, confWithDelta, master)(fn)
  }

  def withSparkSession(
      folderTmp: Option[File] = None,
      defaultConfig: SparkConf = new SparkConf(),
      master: String = "local[*]",
      delta: Boolean = false,
      hive: Boolean = false
  )(
      fn: SparkSession => Unit
  ): Unit = {



    implicit class BuilderEnrich(b: Builder ) {
      def applyDelta: Builder = if (delta) {
        b.config(
          "spark.sql.extensions",
          "io.delta.sql.DeltaSparkSessionExtension"
        ).config(
          "spark.sql.catalog.spark_catalog",
          "org.apache.spark.sql.delta.catalog.DeltaCatalog"
        )
      } else b

      def applyHive: Builder = if (hive) b.enableHiveSupport() else b
    }

    def run(f: File): Unit = {

      val spark = SparkSession
        .builder()
        .config(defaultConfig)
        .applyDelta
        .applyHive
        .config(
          "spark.sql.warehouse.dir",
          (f / "spark-warehouse").toString()
        )
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

    folderTmp match {
      case Some(f) => run(f)
      case None    => withTemporalFolder() { f => run(f) }
    }

  }

}
