package spark.perf

import spark._
import spark.SparkContext._
import spark.broadcast.Broadcast
import spark.util.Vector
import java.util.Random
import scala.math.exp
import scala.collection.mutable.HashMap

object TestBroadcast {
  
  var sc : SparkContext = null

  def testWithArgs(numObjects : Int, numVariables: Int, numTasks: Int) : Long = {
    val startTime = System.currentTimeMillis

    val broadCastVars = HashMap[Int, Broadcast[Array[Int]]]()
    val data = (1 to numObjects).toArray

    (1 to numVariables).foreach { i => 
      broadCastVars(i) = sc.broadcast(data)
    }

    val rdd = sc.parallelize(1 to numTasks, numTasks)

    rdd.map { i =>
      var sum : Long = 0
      (1 to numVariables).foreach { i => 
        sum += broadCastVars(i).value.reduce(_ + _)
      }
      sum
    }.reduce(_ + _)

    System.currentTimeMillis - startTime
  }

  def main(args: Array[String]) {
    val sparkHome = System.getenv("SPARK_HOME")
    sc = new SparkContext(args(0), "TestBroadcast", sparkHome, Nil)

    val result = testWithArgs(args(1).toInt, args(2).toInt, args(3).toInt)
    println(result)

    sc.stop()
  }

}