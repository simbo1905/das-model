package com.github.simbo1905.dasmodel

import org.scalatest.{ FunSuite, Matchers}

object DASModelSuite {
  val singleApprentices = Seq(Apprentice(1, 12000))

  val threeApprentices = Seq(Apprentice(1, 1200), Apprentice(7, 2400), Apprentice(12, 3600))
}

class DASModelSuite extends FunSuite with Matchers {
  import DASModelSuite._
  test("Cells can be converted to apprentices.") {
    DASModel.cellsToApprentices(Seq(Cell(6,1,3), Cell(6,2,10000), Cell(7,1,6), Cell(7,2,11000), Cell(8,1,9), Cell(8,2,12000))) shouldBe Seq (
      Apprentice(3,10000),
      Apprentice(6,11000),
      Apprentice(9,12000)
    )
  }
  test("Single year long apprentice can be converted to funds due") {
    val twelveOfOneThousand = (1 to 12) map {
      TrainingDue(_, 1000)
    }
    DASModel.apprenticesToTrainingDue(singleApprentices) shouldBe twelveOfOneThousand
  }
  test("Three apprentices can be converted into funds due") {
    DASModel.apprenticesToTrainingDue(threeApprentices) shouldBe Seq(
      TrainingDue(1,100),
      TrainingDue(2,100),
      TrainingDue(3,100),
      TrainingDue(4,100),
      TrainingDue(5,100),
      TrainingDue(6,100),
      TrainingDue(7,300),
      TrainingDue(8,300),
      TrainingDue(9,300),
      TrainingDue(10,300),
      TrainingDue(11,300),
      TrainingDue(12,600)
    )
  }

}