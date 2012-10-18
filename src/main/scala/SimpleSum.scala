package spark.perf

import spark.SparkContext
import spark.SparkContext._

object SimpleSum {
  
  var sc : SparkContext = null
  
  def testWithArgs(numSlices : Int, numReduceTasks: Int) : Long = {
    (1 to 5).map { i =>
      val startTime = System.currentTimeMillis
      sc.parallelize  ((1 to 1000), numSlices).reduce(_ + _)
      System.currentTimeMillis - startTime
    }.min
  }
  
  def main(args: Array[String]) {
    sc = new SparkContext(System.getenv("MASTER"), "SortBy", 
      System.getenv("SPARK_HOME"), Nil, Util.executorVars)
    val result = testWithArgs(args(0).toInt, args(1).toInt)
    println(result)
    sc.stop()
  }
}
