package com.example.workshop.spark

import org.apache.spark.sql.SparkSession
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import java.nio.file.{Files, Path}
import scala.jdk.CollectionConverters._

class WordCountIT extends AnyFunSuite with Matchers {

  test("WordCount main writes expected output to a directory") {
    val inputDir: Path = Files.createTempDirectory("wordcount-it-in")
    val inputFile = inputDir.resolve("data.txt")
    Files.writeString(inputFile, "a a a\nb b\nSpark spark!\n")

    val outputDir: Path = Files.createTempDirectory("wordcount-it-out")
    // Spark requires output path not to exist
    Files.deleteIfExists(outputDir)

    // Run the job in local mode for ITs
    System.setProperty("spark.master", "local[2]")
    System.setProperty("spark.driver.host", "127.0.0.1")

    // If the job uses SparkSession.builder without master, we can pass master through env:
    // Spark respects SPARK_MASTER or spark.master config; here we set SPARK_MASTER for safety.
    val prev = sys.env.get("SPARK_MASTER")
    // Can't mutate env in JVM reliably; instead we rely on spark.master system property above.

    WordCount.main(Array(inputFile.toString, outputDir.toString))

    val partFiles = Files.list(outputDir).iterator().asScala
      .filter(p => p.getFileName.toString.startsWith("part-"))
      .toList

    partFiles.nonEmpty shouldBe true

    val lines = partFiles.flatMap(p => Files.readAllLines(p).asScala).toList
    // We expect: a=3, b=2, spark=2
    lines.toSet shouldEqual Set("a\t3", "b\t2", "spark\t2")
  }
}
