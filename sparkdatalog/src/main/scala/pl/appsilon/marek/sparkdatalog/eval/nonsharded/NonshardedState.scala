package pl.appsilon.marek.sparkdatalog.eval.nonsharded

import org.apache.spark.SparkContext
import pl.appsilon.marek.sparkdatalog.Database

case class NonshardedState(database: Database, idb: Set[String] = Set(), delta: Database = Database.empty) {
  val sc: SparkContext = database.relations.head._2.data.context

  def checkpoint() = {
    database.checkpoint()
    delta.checkpoint()
  }

  def deltaEmpty: Boolean = delta.isEmpty

  def step(newDatabase: Database) = new NonshardedState(newDatabase, idb, newDatabase.subtract(database, idb))

  def toDatabase: Database = {
    database
  }

  def cache(): this.type = {
    database.cache()
    delta.cache()
    this
  }

  def unpersist(blocking: Boolean = true): this.type = {
    database.unpersist(blocking)
    delta.unpersist(blocking)
    this
  }

  def materialize(): this.type = {
    database.materialize()
    delta.materialize()
    this
  }

  override def toString = {
    "State: " +  database.toString + " Delta: " + delta.toString
  }

  def prepareForIteration(newIdb: Set[String]): NonshardedState = {
    copy(idb = newIdb, delta = database.restrictTo(newIdb))
  }

}

object NonshardedState {
  def fromDatabase(database: Database): NonshardedState = {
    new NonshardedState(database)
  }

}
