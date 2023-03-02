val scalatestVersion = "3.2.15"
val betterFilesVersion = "3.9.2"
val sparkVersion = "3.3.2"
val h2Version = "2.1.214"

lazy val scala212 = "2.12.17"
lazy val scala213 = "2.13.10"
ThisBuild / crossScalaVersions := List(scala212, scala213)
ThisBuild / releaseCrossBuild := true
ThisBuild / scalaVersion := scala212
Test / fork := true

ThisBuild / versionScheme := Some("semver-spec")
ThisBuild / organization := "com.acervera"
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
  "org.scalatest" %% "scalatest" % scalatestVersion % Compile,
  "com.h2database" % "h2" % h2Version % Compile,
  "com.github.pathikrit" %% "better-files" % betterFilesVersion % Compile,
  "org.apache.spark" %% "spark-sql" % sparkVersion % Optional
)
