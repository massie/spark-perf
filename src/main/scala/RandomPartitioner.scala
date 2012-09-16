import scala.util.Random
import spark.Partitioner

class RandomPartitioner(partitions: Int) extends Partitioner {
   def numPartitions = partitions
   def getPartition(key: Any): Int = {
     Random.nextInt(numPartitions)
   }
}

