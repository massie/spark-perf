name := "Spark_Perf"

version := "0.1"

scalaVersion := "2.9.2"

unmanagedJars in Compile <++= baseDirectory map  { base =>
  val finder: PathFinder = (file("spark")) ** "*.jar" 
  finder.get
}