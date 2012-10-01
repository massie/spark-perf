// package spark.perf

// import spark._
// import spark.SparkContext._
// import java.util.Random
// import scala.collection.mutable.HashMap

// object TestGroupBy {
  
//   var sc : SparkContext = null

//   def testWithArgs(numPairs: Int, numKeys: Int, numTasks: Int) : Long = {
//     (1 to 5).map { i =>
//       GroupBy.runTest(sc, numPairs, numKeys, numTasks)
//     }.min
//   }

//   def main(args: Array[String]) {
//     val sparkHome = System.getenv("SPARK_HOME")
//     sc = new SparkContext(args(0), "TestGroupBy", sparkHome, Nil)

//     val result = testWithArgs(args(1).toInt, args(2).toInt, args(3).toInt)
//     println(result)

//     sc.stop()

//   }

// }