name := "abctemplates"

version := "0.3.0"

organization := "org.purang.net"

scalaVersion := "2.12.2"

crossScalaVersions := Seq("2.11.8", "2.12.2")

libraryDependencies ++= Seq(
  "org.jsoup" % "jsoup" % "1.10.2" withSources(),
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)

resolvers ++= Seq(
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
)

scalacOptions in ThisBuild ++= Seq(
  "-encoding",
  "UTF-8",
  "-J-Xss64m",
  "-deprecation",
  "-feature",
  "-unchecked",
  //"-Xfatal-warnings",
  "-Xlint", //"-Yno-predef",
  "-Yrangepos",
  "-Yno-adapted-args",
  "-Ywarn-value-discard",
  "-Ywarn-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-inaccessible",
  "-Ywarn-nullary-override",
  "-Ywarn-numeric-widen",
  "-language:implicitConversions",
  "-language:higherKinds",
  "-language:existentials"
) ++ (CrossVersion.partialVersion(scalaVersion.value) match {
  case Some((2, p)) if p >= 12 => Seq(
    "-opt:l:classpath"
  )
  case Some((2, 11)) => Seq(
    "-optimize",
    "-Yinline",
    "-Yinline-warnings"
  )
})

cancelable := true

fork := true

logBuffered := false

parallelExecution in Test := true

licenses += ("BSD", url("http://www.tldrlegal.com/license/bsd-3-clause-license-%28revised%29"))

wartremoverErrors ++= Warts.unsafe

wartremoverWarnings ++= Warts.all
