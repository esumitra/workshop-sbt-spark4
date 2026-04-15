package com.example.workshop.basics.session2

object GradeManager {

  def determineOverallStatus(subjectGrades: List[SubjectGrade]): String = ???

  def getTenthGradeStudents(students: List[Student]): List[Student] = ???

  def identifyHighAchievers(subjectGrades: List[SubjectGrade], threshold: Double = 80): List[(String, String)] = ???

  def calculateClassGPA(studentIds: List[String], studentScores: Map[String, List[Double]]): List[Double] = ???
}