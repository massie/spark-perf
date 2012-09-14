import scala.util.Random
import scala.math
import spark.SparkContext
import spark.SparkContext._

object RandomStrings {
  val sc = new SparkContext("local","RandomStrings")

  /**
   * Create an rdd of numPairs key-value pairs with approximately numKeys distinct keys
   * @param keyLen length of key
   * @param valueLen length of value
   * @param numPairs total number of pairs
   * @param numKeys approximate number of distinct keys
   */
  def generatePairs(keyLen: Int, valueLen: Int, numPairs: Int, numKeys: Int): 
    spark.RDD[(String,String)] = {

    val numPartitions = 10

    val rdd = sc.parallelize(1 to numPartitions,numPartitions).flatMap { partition =>
      var numPartitionPairs = numPairs/numPartitions
      var numPartitionKeys = numKeys/numPartitions

      // have last task handle tail
      if (partition == numPartitions) {
        numPartitionPairs += numPairs % numPartitions
        numPartitionKeys += numKeys % numPartitions
      }
      
      // generate the keys used
      val keys = new Array[String](numPartitionKeys)
      for (i <- 0 until numPartitionKeys) {
        keys(i) = Random.nextString(keyLen)
      }
      
      // A dummy key is used to randomly shuffle the pairs across partitions.
      // The actual pairs are kept as the value.
      val pairs = new Array[(Int,(String,String))](numPartitionPairs)
      for (i <- 0 until numPartitionPairs) {
        val keyIndex = (math.random * numPartitionKeys).toInt
	val dummyKey = Random.nextInt
	pairs(i) = (dummyKey,(keys(keyIndex),Random.nextString(valueLen)))
      }
      pairs
    } sortByKey() map { // Sort by dummy key to shuffle pairs and then remove dummy key
      pair => pair._2 
    }

    rdd
  }

  def main(args: Array[String]) {
    RandomStrings.generatePairs(10,5,2002,50).saveAsSequenceFile("randomstrings")
  }
}
