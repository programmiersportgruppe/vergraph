scalaVersion := "2.11.0"

libraryDependencies ++=
  "org.parboiled" %% "parboiled-scala" % "1.1.6" ::
  "org.scala-lang.modules" %% "scala-xml" % "1.0.1" ::
  Nil

libraryDependencies ++= {
  "org.scalatest" %% "scalatest" % "2.1.6" ::
  Nil
}.map(_ % "test")
