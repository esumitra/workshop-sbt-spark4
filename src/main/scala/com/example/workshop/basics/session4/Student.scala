package com.example.workshop.basics.session4

case class Student(id: Int, name: String, scores: List[Double])

object Student {
  val allStudents = List(
    Student(1, "Amara Osei",    List(92.0, 88.5, 95.0, 91.0)),
    Student(2, "Lena Kovač",    List(74.0, 68.0, 72.5, 70.0)),
    Student(3, "Tariq Nasser",  List(55.0, 60.0, 58.5, 62.0)),
    Student(4, "Yuki Tanaka",   List(83.0, 87.0, 90.5, 85.0)),
    Student(5, "Sofia Reyes",   List(95.0, 98.0, 100.0, 97.5))
  )

}
