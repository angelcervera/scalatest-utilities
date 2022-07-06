val scalatestVersion = "3.2.12"
val betterFilesVersion = "3.9.1"
val sparkVersion = "3.2.1"
val h2Version = "2.1.214"

ThisBuild / versionScheme := Some("semver-spec")
ThisBuild / version := "0.2.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.8"
ThisBuild / organization := "com.acervera"
ThisBuild / homepage := Some(
  url(s"https://simplexspatial.github.io/osm4scala/")
)
ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/simplexportal/scalatest-utilities"),
    "scm:git:git://github.com/simplexportal/scalatest-utilities.git",
    "scm:git:ssh://github.com:simplexportal/scalatest-utilities.git"
  )
)
ThisBuild / developers := List(
  Developer(
    "angelcervera",
    "Angel Cervera Claudio",
    "angelcervera@simplexportal.com",
    url("https://www.acervera.com")
  ),
  Developer(
    "angelcr",
    "Angel Cervera Roldan",
    "angelcr@simplexportal.com",
    url("https://www.angelcerveraroldan.com")
  )
)

organizationHomepage := Some(url("https://www.simplexportal.com"))
name := "scalatest-utilities"

// https://www.scala-sbt.org/1.x/docs/Publishing.html
credentials += Credentials(Path.userHome / ".sbt" / ".credentials")
publishTo := {
  val awsCodeArtifact = "http://repo.simplexportal.com:9181/repository"
  val repo =
    if (isSnapshot.value)
      "maven-simplexportal-snapshots" at awsCodeArtifact + "/maven-simplexportal-snapshots/"
    else
      "maven-simplexportal-releases" at awsCodeArtifact + "/maven-simplexportal-releases/"
  Some(repo.withAllowInsecureProtocol(true))
}

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % scalatestVersion % Optional,
  "com.h2database" % "h2" % h2Version % Optional,
  "com.github.pathikrit" %% "better-files" % betterFilesVersion % Optional,
  "org.apache.spark" %% "spark-sql" % sparkVersion % Optional
)
