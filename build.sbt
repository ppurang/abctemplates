name := "abctemplates"

version := "0.5.0"

organization := "org.purang.net"

scalaVersion := "2.13.4"

libraryDependencies ++= Seq(
  "org.jsoup" % "jsoup" % "1.13.1" withSources(),
  "org.scalatest" %% "scalatest" % "3.2.4" % "test"
)

cancelable := true

fork := true

logBuffered := false

parallelExecution in Test := true

licenses += ("BSD", url("http://www.tldrlegal.com/license/bsd-3-clause-license-%28revised%29"))

wartremoverErrors ++= Warts.unsafe

wartremoverWarnings ++= Warts.all
