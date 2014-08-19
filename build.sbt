name := "abctemplates"

version := "0.2.1"

organization := "org.purang.net"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "org.jsoup" % "jsoup" % "1.7.3" withSources(),
  "org.scalaz" %% "scalaz-core" % "7.0.6"  withSources(),
  "org.scalaz" %% "scalaz-concurrent" % "7.0.6"  withSources(),
  "org.scalacheck" %% "scalacheck" % "1.11.4" % "test",
  "org.scalatest" %% "scalatest" % "2.1.6" % "test",
  "com.github.axel22" %% "scalameter" % "0.5-M2" % "test"
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
