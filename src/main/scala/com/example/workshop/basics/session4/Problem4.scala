package com.example.workshop.basics.session4

object Problem4 {
  val students = Student.allStudents
  
  def buildMessage(prefix: String)(student: Student)(avg: Double): String = ???

  val alertMessage: Student => Double => String = ???
  val infoMessage:  Student => Double => String = ???

  def scoreFilter(threshold: Double)(op: (Double, Double) => Boolean)(student: Student): Boolean = ???

  val aboveThreshold: Student => Boolean = ???
  val belowThreshold: Student => Boolean = ???
  
}
