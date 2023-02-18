ThisBuild / name := "abctemplates"

ThisBuild / version := "3.2.1" //we will follow milestones from scala effect 3

ThisBuild / organization := "org.purang.templates"

ThisBuild / scalaVersion := "3.2.2"

ThisBuild / crossScalaVersions := Seq("3.2.2", "2.13.10")

ThisBuild / versionScheme := Some("early-semver")

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
  "org.jsoup"      % "jsoup"       % "1.15.4",
  "org.typelevel" %% "cats-effect" % "3.4.6",
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
