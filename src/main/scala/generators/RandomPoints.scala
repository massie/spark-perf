package spark.perf

import scala.math
import scala.util.Random
import spark.util.Vector
import spark.SparkContext
import spark.SparkContext._

object RandomPoints {
  
  def generatePoint(dim: Int, offset : Double = 0) : Vector = {
    Vector(dim, Int => ((math.random * 2) - 1) + offset)
  }

  def generateClusteredPoints(sc: SparkContext, numClusters: Int, numPoints: Int, 
    vectorDim: Int, numPartitions: Int) : spark.RDD[Vector] = {

    val centers = (1 to numClusters).map(x => numClusters * math.random)
    sc.parallelize(1 to numPoints, numPartitions).map { x =>
      generatePoint(vectorDim, centers(Random.nextInt(numClusters)))
    }  
  }

}
