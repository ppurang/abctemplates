ThisBuild / name := "abctemplates"

ThisBuild / version := "3.1.0-RC4" //we will follow milestones from scala effect 3

ThisBuild / organization := "org.purang.templates"

ThisBuild / scalaVersion := "3.0.0-RC2"

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
  "org.typelevel" %% "cats-effect" % "3.0.1",
  "org.scalameta" %% "munit" % "0.7.23"  % Test
).map(_ withSources ())

ThisBuild / testFrameworks += new TestFramework("munit.Framework")

ThisBuild / licenses += ("BSD", url(
  "http://www.tldrlegal.com/license/bsd-3-clause-license-%28revised%29"
))

(ThisBuild/ sbt.Compile / doc / sources) := Seq()
(ThisBuild / packageSrc / publishArtifact) := true
(ThisBuild / Test / packageSrc / publishArtifact) := false

publishTo := sonatypePublishToBundle.value
