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
  def runTest(numPairs: Int, numKeys: Int, numTasks: Int): Double = {
    val startTime = System.currentTimeMillis
    RandomStrings.generatePairs(10,10,numPairs,numKeys).groupByKey(numTasks).count()
    (System.currentTimeMillis - startTime) / 1000.0
  }
  
  def main(args: Array[String]) {
    val numPairs = args(0).toInt
    val numKeys = args(1).toInt
    val numTasks = args(2).toInt

    val time = runTest(numPairs,numKeys,numTasks)
    
    println("GroupBy: " + time + " seconds")
  }
}
