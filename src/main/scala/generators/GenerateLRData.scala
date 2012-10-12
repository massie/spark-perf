// package spark.perf

// import spark.SparkContext

// object GenerateLRData {
//   def generatePoints(sc: SparkContext, numPoints: Int, numPartitions: Int) = {
//     val positive = RandomPoints.generateClusteredPoints(sc, 1, numPoints/2, numPartitions)
//     val negative = RandomPoints.generateClusteredPoints(sc, 1, numPoints/2, numPartitions)
//     positive.map { point =>
//       (point, 1)
//     } union negative.map { point =>
//       (point, -1)
//     }
//   }

//   def generatePointsToFile(sc: SparkContext, numPoints: Int, numPartitions: Int, outputDir: String) {
//     generatePoints(sc, numPoints, numPartitions).map { point =>
//       point._2 + " " + point._1.elements.mkString(" ")
//     }.saveAsTextFile(outputDir)
//   }

//   def main(args: Array[String]) {
//     val sparkHome = System.getenv("SPARK_HOME")
//     val jars = List(System.getenv("SPARK_PERF"))
//     val sc = new SparkContext(args(0), "GenerateLRData", sparkHome, jars) 
//     val numPoints = args(1).toInt
//     val numPartitions = args(2).toInt
//     val outputDir = args(3)
//     generatePointsToFile(sc, numPoints, numPartitions, outputDir)
//     sc.stop()
//   }
// }
