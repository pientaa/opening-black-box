lazy val root = (project in file(".")).
  settings(
    name := "udf-detector",
    version := "1.0",
    scalaVersion := "2.11.4",
    mainClass in Compile := Some("src.main.scala")
  )

val sparkVersion = "2.4.5"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
  "org.postgresql" % "postgresql" % "42.2.12" % "provided",
  "com.sparkjava" % "spark-core" % "1.0" % "provided",
  "org.scalaj" %% "scalaj-http" % "2.4.2" % "provided",
  // https://mvnrepository.com/artifact/com.google.guava/guava
  "com.google.guava" % "guava" % "29.0-jre"
)

// META-INF discarding
mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) => {
  case PathList("META-INF", xs@_*) => MergeStrategy.discard
  case x => MergeStrategy.first
}
}