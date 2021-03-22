ThisBuild / name := "abctemplates"

ThisBuild / version := "3.1.0-RC3" //we will follow milestones from scala effect 3

ThisBuild / organization := "org.purang.templates"

ThisBuild / scalaVersion := "3.0.0-RC1"

ThisBuild / scalacOptions ++= Seq(
  "-encoding",
  "UTF-8",
  "-feature",
  "-unchecked",
  "-Xfatal-warnings",
  "-deprecation",
  "-language:implicitConversions",
  "-Ykind-projector"
)

ThisBuild / libraryDependencies ++= Seq(
  "org.jsoup" % "jsoup" % "1.13.1",
  "org.typelevel" %% "cats-effect" % "3.0.0-RC3",
  "org.scalameta" %% "munit" % "0.7.22"  % Test
).map(_ withSources ())

ThisBuild / testFrameworks += new TestFramework("munit.Framework")

ThisBuild / licenses += ("BSD", url(
  "http://www.tldrlegal.com/license/bsd-3-clause-license-%28revised%29"
))

This / sources in (sbt.Compile, doc) := Seq()
ThisBuild / publishArtifact in packageSrc := true
ThisBuild / publishArtifact in packageSrc in Test := false

publishTo := sonatypePublishToBundle.value
