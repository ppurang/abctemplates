name := "abctemplates"

version := "3.1.0-RC1" //we will follow milestones from scala 3

organization := "org.purang"

scalaVersion := "3.0.0-RC1"

scalacOptions ++= Seq(
  "-encoding",
  "UTF-8",
  "-feature",
  "-unchecked",
  "-Xfatal-warnings",
  "-deprecation",
  "-language:implicitConversions",
  "-Ykind-projector"
)

libraryDependencies ++= Seq(
  "org.jsoup" % "jsoup" % "1.13.1",
  "org.typelevel" %% "cats-effect" % "3.0.0-RC2",
  "org.scalameta" %% "munit" % "0.7.22" % Test
).map(_ withSources ())

testFrameworks += new TestFramework("munit.Framework")

licenses += ("BSD", url(
  "http://www.tldrlegal.com/license/bsd-3-clause-license-%28revised%29"
))
