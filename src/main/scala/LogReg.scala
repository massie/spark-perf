package spark.perf

import spark._
import spark.SparkContext._
import spark.util.Vector
import java.util.Random
import scala.math.exp
import scala.collection.mutable.HashMap

case class DataPoint(x: Vector, y: Double)

object LogReg {

  val rand = new Random(42)
  val ITERATIONS = 10
  val R = 5
  var sc : SparkContext = null

  def generateData(N: Int, D: Int, R: Int, numSlices: Int) = {
    def generatePoint(i: Int) = {
      val y = if(i % 2 == 0) -1 else 1
      val x = Vector(D, _ => rand.nextGaussian + y * R)
      DataPoint(x, y)
    }
    sc.parallelize((1 to N), numSlices).map(generatePoint(_))
  }

  def testWithArgs(rddSlices: Int, numPoints: Int, vectorDim: Int) = {
    val data = generateData(numPoints, vectorDim, R, rddSlices)
    val points = data.cache()

    val startTime = System.currentTimeMillis
    
    // Initialize w to a random value
    var w = Vector(vectorDim, _ => 2 * rand.nextDouble - 1)
    // Gradient Descent
    for (i <- 1 to ITERATIONS) {
      val gradient = points.map { p =>
        (1 / (1 + exp(-p.y * (w dot p.x))) - 1) * p.y * p.x
      }.reduce(_ + _)
      w -= gradient
    }
    
    (System.currentTimeMillis - startTime)
  }


  def main(args: Array[String]) {
    sc = new SparkContext(System.getenv("MASTER"), "LogReg", 
      System.getenv("SPARK_HOME"), Nil, Util.executorVars)
    // Parse arguments
    val Array(rddSlices, numReduceTasks, numPoints, vectorDim) = args.map(_.toInt)
    // Run test
    val time = (1 to 5).map { i => 
      testWithArgs(rddSlices, numPoints, vectorDim)
    }.min
    
    println(time) 
    sc.stop()
  }

}