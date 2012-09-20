package spark.perf

import spark.SparkContext
import spark.SparkContext._

object GroupBy {
  /**
    * Returns time in seconds to perform groupByKey transformation
    * on a set of randomly generated key-value pairs.
    * @param numPairs total number of pairs
    * @param numKeys approximate number of distinct keys
    * @param numTasks number of tasks to use
    */
  def runTest(sc: SparkContext, numPairs: Int, numKeys: Int, numTasks: Int): Double = {
    val pairs = RandomStrings.generatePairs(sc, 10, 10, numPairs, numKeys, numTasks).cache()
    pairs.count()
    val startTime = System.currentTimeMillis
    pairs.groupByKey(numTasks).count()
    (System.currentTimeMillis - startTime) / 1000.0
  }
  
  def main(args: Array[String]) {
    val sparkHome = System.getenv("SPARK_HOME")
    val jars = List(System.getenv("SPARK_PERF"))
    val sc = new SparkContext(args(0), "GroupBy", sparkHome, jars)
    val numPairs = args(1).toInt
    val numKeys = args(2).toInt
    val numTasks = args(3).toInt

    val time = runTest(sc,numPairs,numKeys,numTasks)
    sc.stop()
    println("GroupBy: " + time + " seconds")
  }
}
