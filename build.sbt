ThisBuild / organization := "com.example"
ThisBuild / scalaVersion := "2.13.18"
ThisBuild / version      := "0.1.0-SNAPSHOT"

// Single module named "workshop" (root project)
lazy val workshop = (project in file("."))
  .configs(IntegrationTest)
  .settings(
    name := "workshop",

    // Spark
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % "4.0.2" % Provided,
      "org.apache.spark" %% "spark-sql"  % "4.0.2" % Provided,
      "org.apache.spark" %% "spark-core" % "4.0.2" % Test,
      "org.apache.spark" %% "spark-sql"  % "4.0.2" % Test,
      "org.apache.spark" %% "spark-core" % "4.0.2" % IntegrationTest,
      "org.apache.spark" %% "spark-sql"  % "4.0.2" % IntegrationTest,
      "org.apache.commons" % "commons-pool2" % "2.12.0",

      // Testing
      "org.scalatest" %% "scalatest" % "3.2.19" % Test,
      "org.scalatest" %% "scalatest" % "3.2.19" % IntegrationTest
    ),

    // Use separate config for integration tests
    Defaults.itSettings,

    // Recommended for deterministic tests
    Test / fork := true,
    IntegrationTest / fork := true,

    Test / javaOptions ++= Seq(
      "--add-exports=java.base/sun.nio.ch=ALL-UNNAMED",
      "--add-opens=java.base/java.util=ALL-UNNAMED",
      "--add-opens=java.base/java.lang=ALL-UNNAMED",
      "--add-opens=java.base/java.lang.invoke=ALL-UNNAMED",
      "--add-opens=java.base/java.nio=ALL-UNNAMED"
    ),
    IntegrationTest / javaOptions ++= Seq(
      "--add-exports=java.base/sun.nio.ch=ALL-UNNAMED",
      "--add-opens=java.base/java.util=ALL-UNNAMED",
      "--add-opens=java.base/java.lang=ALL-UNNAMED",
      "--add-opens=java.base/java.lang.invoke=ALL-UNNAMED",
      "--add-opens=java.base/java.nio=ALL-UNNAMED"
    ),
    // Assembly (fat jar) for running in Docker
    assembly / mainClass := Some("com.example.workshop.spark.WordCount"),
    assembly / assemblyJarName := "workshop-assembly.jar",

    // Avoid "provided" Spark deps in the fat jar (Spark is on the image)
    // assembly / assemblyOption := (assembly / assemblyOption).value.copy(includeScala = false),
    assembly / assemblyMergeStrategy := {
      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
      case "reference.conf"              => MergeStrategy.concat
      case x                             => MergeStrategy.first
    }
  )
