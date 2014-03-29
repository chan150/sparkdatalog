package s2g.ast.subgoal

import s2g.eval.{EvaluationState, PartialSolution}
import s2g.ast.exp.Exp

case class GoalAssign(toVariable: String, exp: Exp) extends Subgoal {
  override def solveOn(partialSolution: PartialSolution, evaluationState: EvaluationState): Set[PartialSolution] = {
    val value = exp.evaluate(partialSolution)
    partialSolution.get(toVariable) match {
      case None => Set(partialSolution + (toVariable -> value))
      case Some(`value`) => Set(partialSolution)
      case other => Set()
    }
  }
}