ThisBuild / name := "abctemplates"

ThisBuild / version := "3.3.1" //we will follow milestones from scala effect 3

ThisBuild / organization := "org.purang.templates"

ThisBuild / scalaVersion := "3.3.1"

ThisBuild / crossScalaVersions := Seq("3.3.1", "2.13.12")

ThisBuild / versionScheme            := Some("early-semver")
ThisBuild / fork                     := true
ThisBuild / logBuffered              := false
ThisBuild / Test / parallelExecution := true

ThisBuild / scalacOptions ++= Seq(
  "-encoding",
  "UTF-8",
  "-feature",
  "-unchecked",
  "-Xfatal-warnings",
  "-deprecation",
  "-language:implicitConversions"
) ++ {
  if (scalaVersion.value.matches("^3.")) {
    Seq("-Ykind-projector")
  } else if (scalaVersion.value.matches("^2.12")) {
    Seq("-language:higherKinds")
  } else {
    Seq()
  }
}

ThisBuild / libraryDependencies ++= Seq(
  "org.jsoup"      % "jsoup"       % "1.16.1",
  "org.typelevel" %% "cats-effect" % "3.5.1",
  "org.scalameta" %% "munit"       % "0.7.29" % Test
).map(_ withSources ())

ThisBuild / testFrameworks += new TestFramework("munit.Framework")

ThisBuild / licenses += ("BSD", url(
  "http://www.tldrlegal.com/license/bsd-3-clause-license-%28revised%29"
))

(ThisBuild / sbt.Compile / doc / sources)         := Seq()
(ThisBuild / packageSrc / publishArtifact)        := true
(ThisBuild / Test / packageSrc / publishArtifact) := false

publishTo := sonatypePublishToBundle.value
