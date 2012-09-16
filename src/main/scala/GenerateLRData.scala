import spark.SparkContext


object GenerateLRData {
  def generatePoints(sc: SparkContext, numPoints: Int) = {
    val positive = RandomPoints.generateClusteredPoints(sc, 1, numPoints/2)
    val negative = RandomPoints.generateClusteredPoints(sc, 1, numPoints/2)
    positive.map { point =>
      (point,1)
    } union negative.map { point =>
      (point,-1)
    }
  }

  def generatePointsToFile(sc: SparkContext, numPoints: Int, outputDir: String) {
    generatePoints(sc, numPoints).map { point =>
      point._2 + " " + point._1.elements.mkString(" ")
    }.saveAsTextFile(outputDir)
  }

  def main(args: Array[String]) {
    val sc = new SparkContext(args(0),"GenerateLRData",System.getenv("SPARK_HOME"),List("Spark_Perf.jar"))
    val numPoints = args(1).toInt
    val outputDir = args(2)
    generatePointsToFile(sc,numPoints,outputDir)
  }
}
