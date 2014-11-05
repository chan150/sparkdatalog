package pl.appsilon.marek.sparkdatalog.ast.subgoal

import org.apache.spark.rdd.RDD
import pl.appsilon.marek.sparkdatalog.{Database, Valuation}
import pl.appsilon.marek.sparkdatalog.ast.comparisonoperator.ComparisonOperator
import pl.appsilon.marek.sparkdatalog.ast.exp.{AnalyzedExp, Exp}
import pl.appsilon.marek.sparkdatalog.eval.RelationInstance

case class AnalyzedGoalComparison(left: AnalyzedExp, right: AnalyzedExp, operator: ComparisonOperator) extends AnalyzedSubgoal {
  override def evaluateStatic(valuation: Valuation): Option[Valuation] =
    if(decideStatic(valuation))
      Some(valuation)
    else
      None

  def decideStatic(valuation: Valuation): Boolean = {
    operator.decide(left.evaluate(valuation) - right.evaluate(valuation))
  }

  override def solveOn(valuation: Valuation, relations: Map[String, RelationInstance]): Seq[Valuation] = evaluateStatic(valuation).toSeq

  override def solveOnSet(valuations: Seq[Valuation], relations: Map[String, RelationInstance]): Seq[Valuation] =
    valuations.filter(decideStatic)

  override def getLocation: Option[Int] = None

  override def solveRDD(valuations: RDD[Valuation], database: Database): Option[RDD[Valuation]] = {
    println("FILTER decideStatic")
    Some(valuations.filter(decideStatic))
  }

  override def selectRDD(database: Database): Option[RDD[Valuation]] = ???
}
