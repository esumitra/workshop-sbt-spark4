package com.example.workshop.basics.session2

import scala.util.{Using}
import scala.io.Source

object ReadUtils {
  
  def readFileSafely(filePath: String): Option[List[String]] = {
    Using(Source.fromFile(filePath)) { source =>
      source.getLines().toList
    }.toOption
  }

  def readFileUnsafely(filePath: String): List[String] = {
    Source.fromFile(filePath).getLines().toList
  }
}
