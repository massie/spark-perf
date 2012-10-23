package spark.perf

import spark._
import spark.SparkContext._
import spark.broadcast.Broadcast
import spark.util.Vector
import java.util.Random
import scala.math.exp
import scala.collection.mutable.HashMap

object BroadcastTest {
  
  var sc : SparkContext = null

  def testWithArgs(rddSlices: Int, numObjects : Int, numVariables: Int, objectLength: Int) : Long = {

    val data : Array[String] = (1 to numObjects).map(x => RandomStrings.generateString(objectLength)).toArray
    val broadCastVars = HashMap[Int, Broadcast[Array[String]]]()

    val startTime = System.currentTimeMillis
    (1 to numVariables).foreach { i =>  broadCastVars(i) = sc.broadcast(data) }
    val rdd = sc.parallelize(1 to rddSlices, rddSlices)
    rdd.map { i =>
      var sum : Long = 0
      (1 to numVariables).foreach { i => 
        sum += broadCastVars(i).value.length
      }
      sum
    }.reduce(_ + _)
    System.currentTimeMillis - startTime
    
  }

  def main(args: Array[String]) {
    sc = new SparkContext(System.getenv("MASTER"), "BroadcastTest", 
      System.getenv("SPARK_HOME"), Nil, Util.executorVars)
    val result = testWithArgs(args(0).toInt, args(1).toInt, args(2).toInt, args(3).toInt)
    println(result)
    sc.stop()
  }

}