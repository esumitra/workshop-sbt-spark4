package com.example.workshop.spark

import org.apache.spark.sql.SparkSession
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class WordCountSpec extends AnyFunSuite with Matchers with BeforeAndAfterAll {

  private var spark: SparkSession = _

  override protected def beforeAll(): Unit = {
    spark = SparkSession.builder()
      .appName("wordcount-unit-test")
      .master("local[2]")
      .config("spark.ui.enabled", "false")
      .config("spark.driver.bindAddress", "127.0.0.1")
      .config("spark.driver.host", "127.0.0.1")
      // .config("spark.kryo.registrationRequired", "false")
      // .config("spark.kryo.referenceTracking", "false")
      // .config("spark.io.compression.codec", "lz4")
      // .config("spark.kryo.registrator", "com.example.workshop.spark.TestKryoRegistrator")
      .getOrCreate()
  }

  override protected def afterAll(): Unit = {
    if (spark != null) spark.stop()
  }

  test("countWords counts words case-insensitively and strips punctuation") {
    val rdd = spark.sparkContext.parallelize(Seq(
      "Hello, world!",
      "hello Spark spark"
    ))

    val out = WordCount.countWords(rdd).collect().toMap
    out("hello") shouldEqual 2L
    out("spark") shouldEqual 2L
    out("world") shouldEqual 1L
  }
}
