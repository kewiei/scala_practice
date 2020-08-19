ThisBuild / scalaVersion:="2.12.10"

val sparkVersion = "3.0.0"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
//   "org.apache.spark" %% "spark-streaming" % sparkVersion,
//   "org.apache.spark" %% "spark-streaming-twitter" % sparkVersion
)

lazy val hello = (project in file("."))
  .settings(
    name := "Hello"
  )