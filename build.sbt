name := "ToodleKan"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  guice,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
)

routesImport := Seq.empty
TwirlKeys.templateImports := Seq.empty
