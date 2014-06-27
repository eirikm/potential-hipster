organization := "no.penger"

name := "scala-greenfield"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.1"

val unfilteredVersion = "0.8.0"

libraryDependencies ++= Seq(
  "net.databinder" %% "unfiltered-filter" % unfilteredVersion,
  "net.databinder" %% "unfiltered-specs2" % unfilteredVersion % "test",
  "org.specs2" % "specs2_2.11" % "2.3.12" % "test" )
