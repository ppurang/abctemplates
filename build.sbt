name := "abctemplates"

version := "0.2.2"

organization := "org.purang.net"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "org.jsoup" % "jsoup" % "1.8.1" withSources(),
  "org.scalatest" %% "scalatest" % "2.1.6" % "test"
  )

resolvers ++= Seq(
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
)

scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-feature", "-unchecked", "-language:_", "-optimize", "-Yinline", "-Yinline-warnings" , "-Ywarn-all")

cancelable := true

fork := true

testFrameworks += new TestFramework(
    "org.scalameter.ScalaMeterFramework")

logBuffered := false

parallelExecution in Test := false

seq(bintrayPublishSettings:_*)

licenses += ("BSD", url("http://www.tldrlegal.com/license/bsd-3-clause-license-%28revised%29"))
