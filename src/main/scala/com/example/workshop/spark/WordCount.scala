package com.example.workshop.spark

import org.apache.spark.sql.SparkSession
import org.apache.spark.rdd.RDD

/**
  * Simple Spark WordCount.
  *
  * Usage:
  *   spark-submit ... com.example.workshop.spark.WordCount <input> <output>
  *
  * Output format: "word<TAB>count"
  */
object WordCount {

  /** Pure-ish transformation that is easy to unit test. */
  def countWords(lines: RDD[String]): RDD[(String, Long)] = {
    lines
      .flatMap(_.split("\\W+"))
      .map(_.toLowerCase.trim)
      .filter(_.nonEmpty)
      .map(word => (word, 1L))
      .reduceByKey(_ + _)
      .sortBy({ case (w, c) => (-c, w) }) // deterministic-ish ordering
  }

  def main(args: Array[String]): Unit = {
    if (args.length != 2) {
      System.err.println("Usage: WordCount <input> <output>")
      System.exit(2)
    }

    val inputPath  = args(0)
    val outputPath = args(1)

    val spark = SparkSession.builder()
      .appName("workshop-wordcount")
      .getOrCreate()

    try {
      val lines  = spark.sparkContext.textFile(inputPath)
      val counts = countWords(lines)

      // save as "word<TAB>count"
      counts.map { case (w, c) => s"$w\t$c" }.saveAsTextFile(outputPath)
    } finally {
      spark.stop()
    }
  }
}
