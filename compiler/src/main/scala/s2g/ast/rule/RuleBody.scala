package s2g.ast.rule

import org.apache.spark.rdd.RDD
import s2g.ast.subgoal.{GoalPredicate, Subgoal, SubgoalsTopologicalSort}
import s2g.eval._
import s2g.spark.{Database, StaticEvaluationContext, Valuation}

case class RuleBody(subgoals: Seq[Subgoal]) {

  /** Semantic analysis */
  val (sortedSubgoals, outVariables) = SubgoalsTopologicalSort(subgoals)
  val hasRelationalSubgoal = sortedSubgoals.exists(isRelational)
  if(!hasRelationalSubgoal) throw new SemanticException("Rule must contain at least one relational subgoal.")
  val firstRelationalSubgoal = sortedSubgoals.indexWhere(isRelational)
  val boundVariables = sortedSubgoals.scanLeft(Set[String]())((acc, subgoal) => acc ++ subgoal.getOutVariables).dropRight(1)
  val (constantSubgoals, dynamicSubgoals) = sortedSubgoals.zip(boundVariables).splitAt(firstRelationalSubgoal)
  val constantValuations = constantSubgoals.foldLeft(Set(Valuation()))((valuations, subgoal) => valuations.flatMap(subgoal._1.evaluateStatic))

  def isRelational: (Subgoal) => Boolean = {
    case GoalPredicate(_) => true
    case _ => false
  }

  /** Evaluation */

  def findSolutionsSpark(context: StaticEvaluationContext, fullDatabase: Database, deltaDatabase: Database): Option[RDD[Valuation]] = {
    val firstSubgoal::restSubgoals = dynamicSubgoals
    val initialValuations = firstSubgoal._1.select(constantValuations, firstSubgoal._2, fullDatabase)
    restSubgoals.foldLeft(initialValuations)((valuationsOption, subgoal) =>
      valuationsOption.flatMap(valuations => subgoal._1.join(valuations, subgoal._2, fullDatabase)))
  }

}
