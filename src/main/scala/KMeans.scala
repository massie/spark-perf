package spark.perf

import spark._
import spark.SparkContext._
import java.util.Random
import spark.util.Vector
import scala.collection.mutable.HashMap
import scala.collection.mutable.HashSet

object KMeans {

  val NUM_CLUSTERS = 10
  val CONVERGENCE_DIST = 0.01
  
  // Taken from Spark Examples
  def closestPoint(p: Vector, centers: HashMap[Int, Vector]): Int = {
    var index = 0
    var bestIndex = 0
    var closest = Double.PositiveInfinity
  
    for (i <- 1 to centers.size) {
      val vCurr = centers.get(i).get
      val tempDist = p.squaredDist(vCurr)
      if (tempDist < closest) {
        closest = tempDist
        bestIndex = i
      }
    }
    return bestIndex
  }

  // Taken from Spark Examples
  def testWithArgs(data : RDD[Vector], K : Int, convergeDist: Double) : HashMap[Int, Vector] = {

    var points = data.takeSample(false, K, 42)
    var kPoints = new HashMap[Int, Vector]
    var tempDist = 1.0
    
    for (i <- 1 to points.size) {
      kPoints.put(i, points(i-1))
    }

    while(tempDist > convergeDist) {
      var closest = data.map (p => (KMeans.closestPoint(p, kPoints), (p, 1)))
      
      var pointStats = closest.reduceByKey {case ((x1, y1), (x2, y2)) => (x1 + x2, y1 + y2)}
      
      var newPoints = pointStats.map {pair => (pair._1, pair._2._1 / pair._2._2)}.collect()
      
      tempDist = 0.0
      for (pair <- newPoints) {
        tempDist += kPoints.get(pair._1).get.squaredDist(pair._2)
      }
      
      for (newP <- newPoints) {
        kPoints.put(newP._1, newP._2)
      }
    }

    return kPoints
  }

  def main(args: Array[String]) {
    val sc = new SparkContext(System.getenv("MASTER"), "KMeans", 
      System.getenv("SPARK_HOME"), Nil, Util.executorVars)

    // Parse arguments
    val Array(rddSlices, numPoints, vectorDim) = args.map(_.toInt)

    // Generate random points
    val pointsRdd = RandomPoints.generateClusteredPoints(sc, NUM_CLUSTERS, 
      numPoints, vectorDim, rddSlices).cache
    
    // Run the test
    val time = (1 to 5).map { i => 
      val startTime = System.currentTimeMillis
      testWithArgs(pointsRdd, NUM_CLUSTERS, CONVERGENCE_DIST)
      System.currentTimeMillis - startTime
    }.min
    
    println(time) 
   
    sc.stop()
  }

}