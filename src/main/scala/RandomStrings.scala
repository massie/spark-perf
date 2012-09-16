import scala.util.Random
import scala.math
import spark.Partitioner
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
  def generatePairs(sc: SparkContext, keyLen: Int, valueLen: Int, numPairs: Int, numKeys: Int, numPartitions: Int): 
    spark.RDD[(String,String)] = {

    val rdd = sc.parallelize(1 to numPartitions,numPartitions).flatMap { partition =>
      var numPartitionPairs = numPairs/numPartitions
      var numPartitionKeys = numKeys/numPartitions

      // generate the keys used
      //val keys = new Array[String](numPartitionKeys)
      val keys = (0 until numPartitionKeys).map { i => Random.nextString(keyLen) }
      
      val pairs = new Array[(String,String)](numPartitionPairs)
      for (i <- 0 until numPartitionPairs) {
        val keyIndex = Random.nextInt(numPartitionKeys)
	pairs(i) = (keys(keyIndex),Random.nextString(valueLen))
      }
      pairs
    } partitionBy(new RandomPartitioner(numPartitions))

    rdd
  }

  def main(args: Array[String]) {
    val sc = new SparkContext(args(0),"Random Strings")
    val numPairs = args(1).toInt
    val numKeys = args(2).toInt
    val numPartitions = args(3).toInt
    val outputDir = args(4)
    generatePairs(sc,10,5,numPairs,numKeys,numPartitions).saveAsTextFile(outputDir)
  }
}
