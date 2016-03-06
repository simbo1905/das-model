package com.github.simbo1905.dasmodel

import java.util

import scala.collection.immutable.IndexedSeq

case class LevyDue(val month: Int, val amount: Double)

case class TrainingDue(val month: Int, val amount: Double)

case class AgencyFunding(val month: Int, val amount: Double)

case class EmployerFunding(val month: Int, val amount: Double)

case class Apprentice(val month: Int, val amount: Double)

// not strictly part of the model more of an input thing
case class Cell(row: Int, col: Int, val value: Double)

object DASModel {
  /**
    * Turning a range of cells into Apprentices. Could be upgraded to be generic code that turns any grid into any case class.
    *
    * @param cells
    * @return
    */
  def cellsToApprentices(cells: Iterable[Cell]): Seq[Apprentice] = {
    // shift the selection back to 0,0 coordinate
    val minRow = cells.map(_.row).min
    val minCol = cells.map(_.col).min
    val coordsToValue = cells.map(c => ((c.row - minRow, c.col - minCol), c.value)).toMap
    // how many rows?
    val rows = cells.map(_.row).max - minRow
    // turn each row into an object
    (0 to rows map { row =>
      val month = java.lang.Double.valueOf(coordsToValue.getOrElse((row, 0), 0).toString)
      val amount = java.lang.Double.valueOf(coordsToValue.getOrElse((row, 1), 0).toString)
      Apprentice(month.intValue(), amount)
    }).toSeq
  }

  def cellsToApprentices(cells: java.lang.Iterable[Cell]): util.List[Apprentice] = {
    import scala.collection.JavaConverters._
    cellsToApprentices(cells.asScala).asJava
  }

  /**
    * Calculates 12 months of payments for the current year. Assumes all apprentices are paid over twelve months but start on different months.
    *
    * @param apprentices A sequence of apprentices with the month in the current year that they start.
    * @return The payments to be made in each month.
    */
  def apprenticesToTrainingDue(apprentices: Seq[Apprentice]): Seq[TrainingDue] = {

    val contributions: Seq[IndexedSeq[Double]] = apprentices map {
      case Apprentice(month, amount) =>
        val monthlyAmount = amount / 12
        (1 to 12) map {
            case m if m >= month =>
              monthlyAmount
            case m =>
              0.0
        }
    }

    (0 until 12) map { month =>
      TrainingDue(
        month + 1,
        ((0 until apprentices.size) map { apprentice =>
          contributions(apprentice)(month)
        }).sum
      )
    }
  }

  def apprenticesToTrainingDue(apprentices: java.lang.Iterable[Apprentice]): util.List[TrainingDue] = {
    import scala.collection.JavaConverters._
    apprenticesToTrainingDue(apprentices.asScala.toSeq).asJava
  }

}
