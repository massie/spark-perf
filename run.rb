#!/usr/bin/env ruby

if ARGV.size != 1
  $stderr.puts "Usage: ./run [Git Commit Hash]"
  exit 1
end

# Check if a config file exists
if !File.exists?("#{File.dirname(__FILE__)}/config/config.rb")
  $stderr.puts "Please create a #{File.dirname(__FILE__)}/config/config.rb file."
  exit 1
end

# Clone the Git repository and create a branch for the hash
$stderr.puts "Downloading Spark..."
`git clone git://github.com/mesos/spark.git` unless File.exists?("spark")
$stderr.puts "Building branch #{ARGV[0]}. This may take a while."
`cd spark; git checkout -b #{ARGV[0]} #{ARGV[0]}; sbt/sbt products; sbt/sbt publish-local`
# Building the spark-perf code against the downloaded version of Spark
$stderr.puts "Building spark-perf..."
`cd #{File.dirname(__FILE__)}; sbt/sbt compile`


exec "#{File.dirname(__FILE__)}/script/all.rb"
