# Tests in the list below will be executed in order
TESTS = ["simplesum", "groupby", "sortby", "kmeans", "logreg"]

# Spark Configuration Options
# --------------------------------------------------

# If this is not set the script will look at your env variables
SPARK_HOME = nil
MASTER = "local"

SPARK_MEM = "8g"

# Each element of this list below will be tested with all
# test-level combination defined later in this file
JAVA_OPTS = {
  # Fraction of JVM memory used for caching RDDs
  "spark.storage.memoryFraction" => 0.66,
  # Use coarse-grained mesos scheduling
  "spark.mesos.coarse" => false,
  # Type of serialization used: spark.JavaSerializer, spark.KryoSerializer
  "spark.serializer" => "spark.JavaSerializer",
  # Enable/Disable Compression
  "spark.blockManager.compress" => false
}

# Default RDD Storage Level: 
# NONE, DISK_ONLY, DISK_ONLY_2, 
# MEMORY_ONLY, MEMORY_ONLY_2, MEMORY_ONLY_SER, MEMORY_ONLY_SER_2
# MEMORY_AND_DISK, MEMORY_AND_DISK_2, MEMORY_AND_DISK_SER, MEMORY_AND_DISK_SER_2
RDD_STORAGE_LEVEL = "NONE"


# Global Test Configuration Section
# --------------------------------------------------

# The following parameters are shared among all tests.
# Note that usually all combinations of the parameters below will be executed.

# The number of slices an RDD is split into
RDD_SLICES = [10]

# The default number of reduce tasks 
NUM_REDUCE_TASKS = [10]


# SimpleSum Configuration
# --------------------------------------------------

# No configuration options


# GroupBy Configuration
# --------------------------------------------------

# Toal number of KV-Pairs
GROUPBY_NUM_TUPLES = [100000, 1000000, 10000000]
# Number of distinct keys
GROUPBY_DISTINCT_KEYS = [1000]
# String length of the tuple value (Bytes = approx. 2 * LENGTH + 40)
GROUPBY_VALUE_LENGTH = [100]


# SortBy Configuration
# --------------------------------------------------

# Toal number of KV-Pairs
SORTBY_NUM_TUPLES = [100000, 1000000, 10000000]
# Number of distinct keys
SORTBY_DISTINCT_KEYS = [1000]
# String length of the tuple value
SORTBY_VALUE_LENGTH = [100]


# KMeans Configuration
# --------------------------------------------------

# Total number of points used for KMeans
KMEANS_NUM_POINTS = [100000, 1000000]
# Dimension of the vectors
KMEANS_DIMENSION = [10]


# Logistic Regression Configuration
# --------------------------------------------------

# Total number of points used for KMeans
LR_NUM_POINTS = [100000, 1000000]
# Dimension of the vectors
LR_DIMENSION = [10]

