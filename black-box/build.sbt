
lazy val root = (project in file(".")).
  settings(
    name := "black-box",
    version := "1.0",
    scalaVersion := "2.12.4",
    mainClass in Compile := Some("src.main.scala")
  )

val sparkVersion = "2.4.7"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
  "org.postgresql" % "postgresql" % "42.2.12" % "provided",
  "com.sparkjava" % "spark-core" % "1.0"  % "provided",
  "ch.cern.sparkmeasure" %% "spark-measure" % "0.17" % "provided"
)

// META-INF discarding
mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) => {
  case PathList("META-INF", xs@_*) => MergeStrategy.discard
  case x => MergeStrategy.first
}
}