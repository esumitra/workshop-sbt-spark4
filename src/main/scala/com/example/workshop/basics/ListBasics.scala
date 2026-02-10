package com.example.workshop.basics

object ListBasics {

  /** Squares each integer. */
  def squares(xs: List[Int]): List[Int] =
    xs.map(x => x * x)

  /** Keeps only even integers. */
  def evens(xs: List[Int]): List[Int] =
    xs.filter(_ % 2 == 0)

  /** Sum of all integers (0 for empty list). */
  def sum(xs: List[Int]): Int =
    xs.reduceOption(_ + _).getOrElse(0)

  /** Word frequencies using map + groupBy + mapValues. */
  def wordCounts(words: List[String]): Map[String, Int] =
    words
      .map(_.trim.toLowerCase)
      .filter(_.nonEmpty)
      .groupBy(identity)
      .view
      .mapValues(_.size)
      .toMap
}
