package spark.perf

import spark.SparkContext
import spark.SparkContext._

object GroupBy {
  
  var sc : SparkContext = null
  val KEY_LENGTH = 10

  def testWithArgs(rddSlices : Int, numReduceTasks: Int, numPairs: Int, numKeys: Int, valueLength: Int) : Long = {
    val pairs = RandomStrings.generatePairs(sc, KEY_LENGTH, valueLength, numPairs, numKeys, rddSlices).cache()
    (1 to 5).map { i =>
      val startTime = System.currentTimeMillis
      pairs.groupByKey(numReduceTasks).count
      System.currentTimeMillis - startTime
    }.min
  }
  
  def main(args: Array[String]) {
    sc = new SparkContext(System.getenv("MASTER"), "GroupBy", System.getenv("SPARK_HOME"), Nil, 
      Map("SPARK_HOME" -> System.getenv("SPARK_HOME"),
        "SCALA_HOME" -> System.getenv("SCALA_HOME"))
    )
    val result = testWithArgs(args(0).toInt, args(1).toInt, args(2).toInt, args(3).toInt, args(4).toInt)
    println(result)
    sc.stop()
  }
}
