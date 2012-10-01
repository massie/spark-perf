package spark.perf

import spark.SparkContext
import spark.SparkContext._

object SortBy {

  def warmup(sc: SparkContext, numTasks : Int) {
    val pairs = RandomStrings.generatePairs(sc, 10, 10, 1000, 100, numTasks).cache()
    (0 until 5).foreach { x =>
      pairs.sortByKey().count()
    }
  }

  /**
    * Returns time in seconds to perform sortByKey transformation
    * on a set of randomly generated key-value pairs.
    * @param numPairs total number of pairs
    * @param numKeys approximate number of distinct keys
    * @param numTasks number of tasks to use
    */
  def runTest(sc: SparkContext, numPairs: Int, numKeys: Int, numTasks: Int): Long = {
    val pairs = RandomStrings.generatePairs(sc, 10, 10, numPairs, numKeys, numTasks).cache()
    pairs.count()
    val startTime = System.currentTimeMillis
    pairs.sortByKey().count()
    System.currentTimeMillis - startTime
  }
  
  def main(args: Array[String]) {
    val sparkHome = System.getenv("SPARK_HOME")
    val jars = List(System.getenv("SPARK_PERF"))
    val sc = new SparkContext(args(0), "Sort By", sparkHome, jars)
    val numPairs = args(1).toInt
    val numKeys = args(2).toInt
    val numTasks = args(3).toInt
    
    warmup(sc, numTasks)
    val time = runTest(sc, numPairs, numKeys, numTasks)
    sc.stop()
    println("SortBy: " + time + " seconds")
  }
}
