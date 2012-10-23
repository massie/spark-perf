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
$stderr.puts `git clone git://github.com/mesos/spark.git` unless File.exists?("spark")

# Build Spark
$stderr.puts "Building branch #{ARGV[0]}. This may take a while."
$stderr.puts `cd spark; git checkout -b #{ARGV[0]} #{ARGV[0]}; sbt/sbt package`

# Building the spark-perf code against the downloaded version of Spark
$stderr.puts "Building spark-perf..."
$stderr.puts `cd #{File.dirname(__FILE__)}; sbt/sbt compile`

# Sync the whole directory to the slaves. 
# Here we are assuming we are using our Amazon EC2 AMI, that's a TODO.
$stderr.puts `/root/mesos-ec2/copy-dir #{File.dirname(__FILE__)}` 
  rescue "~/mesos-ec2/copy-dir not found. Continuing without..."

# Run the tests
exec "#{File.dirname(__FILE__)}/script/all.rb"
