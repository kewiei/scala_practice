name := "groupbyage"

version := "0.1"

scalaVersion := "2.12.10"
val sparkVersion = "3.0.0-preview"
fork in run := true
Compile/mainClass := Some(".PopMovie")

lazy val root = (project in file("."))
  .settings(
    name := "scalatest",
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % sparkVersion,
    )

  )