import scala.Math
import scala.util.Random
import spark.util.Vector
import spark.SparkContext
import spark.SparkContext._

object RandomPoints {
  val sc = new SparkContext("local","RandomClusteredPoints")
  
  /**
   * Generates a random point where each element
   * is a value between -range to range
   */
  def generatePoint(dim: Int, range: Double) = {
    Vector(dim, Int => Math.random * 2 * range - range)
  }

  /**
   * Returns rdd of points around random centers.
   * Points in a cluster are uniformly distributed.
   *
   * @param numClusters approximate number of clusters
   * @param numPoints approximate total number of points
   * @param dim number of dimensions for each point
   * @param range elements of generated centers are between -range to range
   * @param radius how widely spread each cluster is
   */
  def generateClusteredPoints(numClusters: Int, numPoints: Int, dim: Int, range: Double, 
    radius: Double) = {

    val numPartitions = 10

    // Assuming a small number of clusters, so just generate centers locally
    val centers = (0 until numClusters).map { i => generatePoint(dim,range) }
    
    sc.parallelize(1 to numPartitions, numPartitions).flatMap { partition =>
      val numPointsPerPartition = numPoints/numPartitions
      
      (0 until numPointsPerPartition).map { i =>
        val center = centers(Random.nextInt(numClusters))
        center + generatePoint(dim,radius)
      }
    }  
  }
  
  def generateClusteredPoints(numClusters: Int, numPoints: Int): spark.RDD[Vector] = {
    generateClusteredPoints(numClusters,numPoints,10,100,10)
  }  
 
  def main(args: Array[String]) {
    val numClusters = args(0).toInt
    val numPoints = args(1).toInt
    val dim = args(2).toInt
    val range = args(3).toDouble
    val radius = args(4).toDouble
    val outputDir = args(5)
    generateClusteredPoints(numClusters,numPoints,dim,range,radius).saveAsTextFile(outputDir)
  }
}
