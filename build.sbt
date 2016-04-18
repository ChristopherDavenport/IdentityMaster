name := "Identity-Master"

mainClass in Compile := Some("createIdentMaster")

scalaVersion := "2.11.8"

libraryDependencies ++= List(
  "com.typesafe.slick" %% "slick" % "3.1.0",
  "org.slf4j" % "slf4j-nop" % "1.7.10",
  "com.h2database" % "h2" % "1.4.187",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "com.typesafe.slick" %% "slick-extensions" % "3.1.0",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.1.0"
)

unmanagedBase := baseDirectory.value / ".lib"

resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/maven-releases/"
fork in run := true
