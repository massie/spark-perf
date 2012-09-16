import spark.SparkContext
import spark.SparkContext._

object SortBy {
  /**
   * Returns time in seconds to perform sortByKey transformation
   * on a set of randomly generated key-value pairs.
   * @param numPairs total number of pairs
   * @param numKeys approximate number of distinct keys
   * @param numTasks number of tasks to use
   */
  def runTest(numPairs: Int, numKeys: Int): Double = {
    val startTime = System.currentTimeMillis
    RandomStrings.generatePairs(10,10,numPairs,numKeys).sortByKey().count()
    (System.currentTimeMillis - startTime) / 1000.0
  }
  
  def main(args: Array[String]) {
    val numPairs = args(0).toInt
    val numKeys = args(1).toInt

    val time = runTest(numPairs,numKeys)
    println("SortBy: " + time + " seconds")
  }
}
