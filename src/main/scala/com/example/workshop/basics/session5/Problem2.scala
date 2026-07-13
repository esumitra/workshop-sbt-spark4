package com.example.workshop.basics.session5

object Problem2 {
  // SUM algebraic data type (ADT) for grade results
  // Using ADTS for modelling pros:
  // - Clear and explicit representation of all possible states
  // - Exhaustive pattern matching ensures all cases are handled
  // - Avoids null values by representing absence explicitly with Option-like ADTs
  sealed trait GradeResult
  case class NotSubmitted() extends GradeResult
  case class Graded(score: Double) extends GradeResult
  case class Excused(reason: String) extends GradeResult

  def describeResult(result: GradeResult): String = ???
  

  val results: List[GradeResult] = List(
    Graded(88.0),
    NotSubmitted(),
    Excused("Medical leave"),
    Graded(45.5),
    Excused("Family emergency")
  )

}
