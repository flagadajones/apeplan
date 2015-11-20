name := """play-getting-started"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  ws
)

libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-compiler" % _ )


libraryDependencies += "org.optaplanner" % "optaplanner-core" % "6.0.1.Final"

resolvers += "Sonatype OSS Snapshots" at "http://central.maven.org/maven2/"
resolvers += "jboss" at "https://repository.jboss.org/nexus/content/groups/public/"