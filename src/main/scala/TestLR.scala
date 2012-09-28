package spark.perf

import spark._
import spark.SparkContext._
import spark.util.Vector
import java.util.Random
import scala.math.exp
import scala.collection.mutable.HashMap

case class DataPoint(x: Vector, y: Double)

object TestLR {

  val rand = new Random(42)
  val ITERATIONS = 10
  var sc : SparkContext = null

  def generateData(N: Int, D: Int, R: Int, numSlices: Int) = {
    def generatePoint(i: Int) = {
      val y = if(i % 2 == 0) -1 else 1
      val x = Vector(D, _ => rand.nextGaussian + y * R)
      DataPoint(x, y)
    }
    sc.parallelize((1 to N), numSlices).map(generatePoint(_))
  }

  def testWithArgs(N: Int, D: Int, R: Int, numSlices: Int) = {
    val data = generateData(N, D, R, numSlices).cache()
    data.count
    val startTime = System.currentTimeMillis
    run(data, numSlices, D)
    val delta = (System.currentTimeMillis - startTime)
    delta
  }

  def run(data: RDD[DataPoint], numSlices: Int, D : Int) = {

    val points = data.cache()

    // Initialize w to a random value
    var w = Vector(D, _ => 2 * rand.nextDouble - 1)

    for (i <- 1 to ITERATIONS) {
      val gradient = points.map { p =>
        (1 / (1 + exp(-p.y * (w dot p.x))) - 1) * p.y * p.x
      }.reduce(_ + _)
      w -= gradient
    }
    w
  }

  def main(args: Array[String]) {
    val sparkHome = System.getenv("SPARK_HOME")
    sc = new SparkContext(args(0), "TestLR", sparkHome, Nil)

    if(args.length == 5){
      val N = args(1).toInt
      val D = args(2).toInt
      val R = args(3).toInt
      val numSlices = args(4).toInt
      val time = testWithArgs(N, D, R, numSlices)
      println((N,D,R,numSlices) + " , " + time) 
    } else {
      val NList = List(10000, 100000, 1000000, 2000000, 5000000, 10000000)
      val DList = List(10)
      val RList = List(1)
      val numSlicesList = List(10, 50, 100, 1000)

      val argsList = (for (n <- NList; d <- DList; r <- RList; s <- numSlicesList) 
        yield (n, d, r, s))

      val results = HashMap[Any, Long]()
      for (i <- (1 to 5)) {
        for (args <- argsList) {
          val (n,d,r,numSlices) = args
          val key = (i, n,d,r,numSlices)
          val time = testWithArgs(n,d,r,numSlices)
          results(key) = time
          println(key + " , " + time) 
        }
      }
      results.foreach { case(k,v) =>  println(k + " : " + v) }

    }

    sc.stop()
    
  }

}