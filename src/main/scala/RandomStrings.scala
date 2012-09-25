package spark.perf

import scala.util.Random
import scala.math
import spark.HashPartitioner
import spark.SparkContext
import spark.SparkContext._

object RandomStrings {
  def generateString(len: Int) = {
    //generates a more readable string than Random.nextString
    (0 until len).map (i => Random.nextPrintableChar) mkString("")
  }
  /**
    * Create an rdd of numPairs key-value pairs with approximately numKeys distinct keys
    * @param keyLen length of key
    * @param valueLen length of value
    * @param numPairs approximate total number of pairs
    * @param numKeys approximate number of distinct keys
    */
  def generatePairs(sc: SparkContext, keyLen: Int, valueLen: Int, numPairs: Int, 
    numKeys: Int, numPartitions: Int): spark.RDD[(String, String)] = {

    val rdd = sc.parallelize(1 to numPartitions, numPartitions).flatMap { partition =>
      var numPartitionPairs = numPairs/numPartitions
      var numPartitionKeys = numKeys/numPartitions

      // generate the keys used
      val keys = (0 until numPartitionKeys).map { i => generateString(keyLen) }
      
      (0 until numPartitionPairs).map { i =>
        val keyIndex = if (i < numPartitionKeys) i else Random.nextInt(numPartitionKeys)
	(Random.nextInt(), (keys(keyIndex), generateString(valueLen)))
      }
      
    } partitionBy(new HashPartitioner(numPartitions)) map (pair => pair._2)

    rdd
  }

  def main(args: Array[String]) {
    val sparkHome = System.getenv("SPARK_HOME")
    val jars = List(System.getenv("SPARK_PERF"))
    val sc = new SparkContext(args(0), "Random Strings", sparkHome, jars)
    val numPairs = args(1).toInt
    val numKeys = args(2).toInt
    val numPartitions = args(3).toInt
    val outputDir = args(4)
    generatePairs(sc, 10, 5, numPairs, numKeys, numPartitions).saveAsTextFile(outputDir)
    sc.stop()
  }
}
