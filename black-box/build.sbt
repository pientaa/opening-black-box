lazy val root = (project in file(".")).settings(
  name := "black-box",
  version := "1.0",
  scalaVersion := "2.12.13",
  mainClass in Compile := Some("src.main.scala"),
  mainClass in Test := Some("src.test.scala")
)

val sparkVersion = "3.0.2"

libraryDependencies ++= Seq(
  "org.apache.spark"     %% "spark-core"       % sparkVersion % "provided",
  "org.apache.spark"     %% "spark-sql"        % sparkVersion % "provided",
  "org.postgresql"        % "postgresql"       % "42.2.19"    % "provided",
  "com.sparkjava"         % "spark-core"       % "1.0"        % "provided",
  "ch.cern.sparkmeasure" %% "spark-measure"    % "0.17"       % "provided",
  "org.scalatest"        %% "scalatest"        % "3.0.1"      % "test",
  "com.github.mrpowers"  %% "spark-fast-tests" % "1.0.0"      % "test"
)

// META-INF discarding
mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
  {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case x                             => MergeStrategy.first
  }
}
