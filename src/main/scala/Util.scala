package spark.perf

import scala.collection.mutable.Map


object Util {

  def executorVars : Map[String, String] = {
    val vars = Map[String, String]()
    Seq("SPARK_HOME", "SCALA_HOME", "MESOS_NATIVE_LIBRARY").foreach { x =>
      if (System.getenv(x) != null) {
        vars.put(x, System.getenv(x))
      }
    }
    vars
  } 

}