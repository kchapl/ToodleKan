name := "ToodleKan"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.19"

libraryDependencies ++= Seq(
  guice,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.3" % Test
)

scalafmtOnCompile := true

routesImport := Seq.empty
TwirlKeys.templateImports := Seq.empty
