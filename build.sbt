name := """PlayIdea"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "org.apache.directory.studio" % "org.apache.commons.io" % "2.4"
)

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.18"
