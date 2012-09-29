package spark.perf

import spark._
import spark.SparkContext._
import java.util.Random
import scala.collection.mutable.HashMap

object TestSortBy {
  
  var sc : SparkContext = null
  val numPairsList = List(100000, 1000000, 5000000, 10000000, 20000000, 50000000)
  val numKeysList = List(100, 1000, 10000)
  val numTasksList = List(10, 20, 100, 500)
  val results = HashMap[Any, Long]()

  def testWithArgs(numPairs: Int, numKeys: Int, numTasks: Int) : Long = {
    return SortBy.runTest(sc, numPairs, numKeys, numTasks)
  }

  def main(args: Array[String]) {
    val sparkHome = System.getenv("SPARK_HOME")
    sc = new SparkContext(args(0), "TestSortBy", sparkHome, Nil)

    if (args.length == 4){
      val result = testWithArgs(args(1).toInt, args(2).toInt, args(3).toInt)
      println(result)
    } else {
      val argsList = (for (x <- numPairsList; y <- numKeysList; z <- numTasksList) 
        yield (x, y, z)).filter { case(x,y,z) => (y <= x && y >= z) }
      
      val results = HashMap[Any, Long]()
      for (i <- (1 to 5)) {
        for (args <- argsList) {
          val (numPairs, numKeys, numTasks) = args
          val key = (i, numPairs, numKeys, numTasks)
          val time = testWithArgs(numPairs, numKeys, numTasks)
          results(key) = time
          println(key + " , " + time) 
        }
      }
      results.foreach { case(k,v) =>  println(k + " : " + v) }
    }

    sc.stop()

  }

}