package com.example.workshop.basics.session4

object Problem5 {
  
  def imperativeStudentReport(students: List[Student]): Unit = {
    var report = ""
    var i = 0
    while (i < students.length) {
      val avg = Problem1.average(students(i).scores)
      if (avg > 80.0) {
        report = report + students(i).name + ": " + avg.toString + "\n"
      }
      i += 1
    }
    println(report)
  }

  
}
