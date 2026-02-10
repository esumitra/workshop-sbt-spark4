package com.example.workshop.basics

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class ListBasicsSpec extends AnyFunSuite with Matchers {

  test("squares maps each element") {
    ListBasics.squares(List(1, 2, 3)) shouldEqual List(1, 4, 9)
  }

  test("evens filters only even numbers") {
    ListBasics.evens(List(1, 2, 3, 4, 5, 6)) shouldEqual List(2, 4, 6)
  }

  test("sum reduces or returns 0 for empty list") {
    ListBasics.sum(List(1, 2, 3, 4)) shouldEqual 10
    ListBasics.sum(Nil) shouldEqual 0
  }

  test("wordCounts normalizes case and ignores empties") {
    val counts = ListBasics.wordCounts(List("Hello", "hello", "  ", "WORLD", "world"))
    counts shouldEqual Map("hello" -> 2, "world" -> 2)
  }
}
