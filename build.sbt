name := "akkahttp-quickstart"

version := "0.1"

scalaVersion := "2.12.6"

val circeVersion = "0.10.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.21",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.21" % Test,
  "com.typesafe.akka" %% "akka-stream" % "2.5.21",
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.21" % Test,
  "com.typesafe.akka" %% "akka-http" % "10.1.7",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.7" % Test,
  "de.heikoseeberger" %% "akka-http-circe" % "1.24.3",
  "org.scalatest" %% "scalatest" % "3.0.5" % Test
) ++ 
  Seq("io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
  ).map(_ % circeVersion)