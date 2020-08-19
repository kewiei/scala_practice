scalaVersion:="2.11.12"

val sparkVersion="2.4.5"
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  // "org.apache.spark" %% "spark-sql" % sparkVersion,
//   "org.apache.spark" %% "spark-streaming" % sparkVersion,
//   "org.apache.spark" %% "spark-streaming-twitter" % sparkVersion
)
lazy val popmovie = (project in file("."))
  .settings(
    name := "popularMovie"
  )