package spark.perf

import spark.SparkContext
import spark.SparkContext._

/** For convenience of running groupby and sortby 
  * with a bunch of different arguments.
  */
object TestAll {
  val pairsKeys = List((1000000, 100),
                       (1000000, 10000),
                       (1000000, 1000000))
  
  def main(args: Array[String]) {
    val sparkHome = System.getenv("SPARK_HOME")
    val jars = List("Spark_Perf.jar")
    val sc = new SparkContext(args(0), "Test All", sparkHome, jars)
    val numTasks = args(1).toInt
    val scale = args(2).toInt
 
    val groupByArgs = pairsKeys.map { case (numPairs, numKeys) =>
      (numPairs * scale, numKeys * scale)
    }
    val sortByArgs = groupByArgs

    val groupByTimes = groupByArgs.map { case (numPairs, numKeys) =>
      GroupBy.runTest(sc, numPairs, numKeys, numTasks)
    }.zip(groupByArgs)
 
    val sortByTimes = sortByArgs.map { case (numPairs, numKeys) =>
      SortBy.runTest(sc, numPairs, numKeys, numTasks)
    }.zip(sortByArgs)
    
    sc.stop()
    sortByTimes.foreach { case (time, (numPairs, numKeys)) =>
      println("SortBy with " + numPairs + " pairs, " + numKeys + " keys: " + time + " seconds")
    }
    groupByTimes.foreach { case (time,(numPairs,numKeys)) =>
      println("GroupBy with " + numPairs + " pairs, " + numKeys + " keys: " + time + " seconds")
    }   
  }
}
