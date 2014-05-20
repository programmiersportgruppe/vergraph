scalaVersion := "2.11.1"

libraryDependencies ++=
  "org.parboiled" %% "parboiled" % "2.0.0-RC1" ::
  "org.scala-lang" % "scala-compiler" % scalaVersion.value ::
  "org.scala-lang.modules" %% "scala-xml" % "1.0.2" ::
  Nil

libraryDependencies ++= {
  "org.scalatest" %% "scalatest" % "2.2.0-M1" ::
  Nil
}.map(_ % "test")
