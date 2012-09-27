package spark.perf

import spark.SparkContext
import spark.SparkContext._
import scala.collection.mutable.Map

/** For convenience of running groupby and sortby 
  * with a bunch of different arguments.
  */
class TestAll(sc: SparkContext) {

  def warmup() {
    GroupBy.warmup(sc)
    SortBy.warmup(sc)
  }

  def runWithArgs(numPairs : Int, numKeys : Int, numTasks : Int) {
    val groupResult = GroupBy.runTest(sc, numPairs, numKeys, numTasks)
    val sortResult = SortBy.runTest(sc, numPairs, numKeys, numTasks)
    println("GroupBy " + (numPairs, numKeys, numTasks) + " : " + groupResult)
    println("SortBy " + (numPairs, numKeys, numTasks) + " : " + sortResult)
  }

  def run() {
    val numPairsList = List(100000, 1000000, 5000000, 10000000, 20000000)
    val numKeysList = List(100, 1000, 10000)
    val numPartitionsList = List(10)
    val argsList = (for (x <- numPairsList; y <- numKeysList; z <- numPartitionsList) 
      yield (x, y, z)).filter { case(x,y,z) => y >= z }


    val groupByResults = Map[Tuple4[Int, Int, Int, Int], Double]()
    val sortByResults = Map[Tuple4[Int, Int, Int, Int], Double]()
    for (i <- (1 to 5)) {
      for (args <- argsList){
        val key = (i, args._1, args._2, args._3)
        println("Running " + key);
        groupByResults(key) = GroupBy.runTest(sc, args._1, args._2, args._3)
        sortByResults(key) = SortBy.runTest(sc, args._1, args._2, args._3)
        println("SortBy " + key + " : " + sortByResults(key))
        println("GroupBy " + key + " : " + groupByResults(key))
      }
    }

    groupByResults.foreach { case(k,v) =>  println("GroupBy " + k + " : " + v) }
    sortByResults.foreach { case(k,v) =>  println("GroupBy " + k + " : " + v) }
  }

  def stop() {
    sc.stop()
  }
}

object TestAll {

  def main(args: Array[String]) {
    val sparkHome = System.getenv("SPARK_HOME")
    val sc = new SparkContext(args(0), "Test All", sparkHome, Nil)
    val test = new TestAll(sc)

    test.warmup()
    if (args.length == 4) {
      test.runWithArgs(args(1).toInt, args(2).toInt, args(3).toInt)
    } else {
      test.run()
    }
    test.stop()
  }

}
