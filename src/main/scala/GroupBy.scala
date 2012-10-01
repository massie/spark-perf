package spark.perf

import spark.SparkContext
import spark.SparkContext._

object GroupBy {
  
  var sc : SparkContext = null

  def testWithArgs(numPairs: Int, numKeys: Int, numTasks: Int) : Long = {
    val pairs = RandomStrings.generatePairs(sc, 100, 100, numPairs, numKeys, numTasks).cache()
    (1 to 5).map { i =>
      val startTime = System.currentTimeMillis
      pairs.groupByKey(numTasks).count
      System.currentTimeMillis - startTime
    }.min
  }
  
  def main(args: Array[String]) {
    val sparkHome = System.getenv("SPARK_HOME")
    sc = new SparkContext(args(0), "GroupBy", sparkHome, Nil)
    val result = testWithArgs(args(1).toInt, args(2).toInt, args(3).toInt)
    println(result)
    sc.stop()
  }
}
