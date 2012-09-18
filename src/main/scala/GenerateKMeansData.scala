package spark.perf

import spark.SparkContext

object GenerateKMeansData {
  def generatePointsToFile(sc: SparkContext, numPoints: Int, numClusters: Int, outputDir: String) {
    val points = RandomPoints.generateClusteredPoints(sc, numClusters, numPoints, 10, 100, 1)
    points.map { point =>
      point.elements.mkString(" ")
    }.saveAsTextFile(outputDir)
  }

  def main(args: Array[String]) {
    val sparkHome = System.getenv("SPARK_HOME")
    val jars = List("Spark_Perf.jar")
    val sc = new SparkContext(args(0), "GenerateKMeansData", sparkHome, jars)
    val numPoints = args(1).toInt
    val numClusters = args(2).toInt
    val outputDir = args(3)
    generatePointsToFile(sc, numPoints, numClusters, outputDir)
    sc.stop()
  }
}
