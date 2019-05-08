name := "brainfuck"

version := "0.1"

scalaVersion := "2.12.8"
libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2"
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.4"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.4" % "test"

fork := true
run / connectInput := true
outputStrategy := Some(StdoutOutput)