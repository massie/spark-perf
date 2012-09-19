package spark.perf

import scala.util.Random
import scala.math
import spark.HashPartitioner
import spark.SparkContext
import spark.SparkContext._

object RandomStrings {
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
      val keys = (0 until numPartitionKeys).map { i => Random.nextString(keyLen) }
      
      (0 until numPartitionPairs).map { i =>
        val keyIndex = Random.nextInt(numPartitionKeys)
	(keys(keyIndex), Random.nextString(valueLen))
      }
      
    } partitionBy(new RandomPartitioner(numPartitions))

    rdd
  }

  def main(args: Array[String]) {
    val sc = new SparkContext(args(0), "Random Strings")
    val numPairs = args(1).toInt
    val numKeys = args(2).toInt
    val numPartitions = args(3).toInt
    val outputDir = args(4)
    generatePairs(sc, 10, 5, numPairs, numKeys, numPartitions).saveAsTextFile(outputDir)
    sc.stop()
  }
}
