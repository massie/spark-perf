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
# Pull just in case spark was already cloned earlier
$stderr.puts `cd spark; git pull origin master:master`

# Build Spark
$stderr.puts "Building branch #{ARGV[0]}. This may take a while."
$stderr.puts `cd spark; git reset --hard #{ARGV[0]}; sbt/sbt package`

# Building the spark-perf code against the downloaded version of Spark
$stderr.puts "Building spark-perf..."
$stderr.puts `cd #{File.dirname(__FILE__)}; sbt/sbt compile`

# Sync the whole directory to the slaves. 
# Here we are assuming we are using our Amazon EC2 AMI, that's a TODO.
$stderr.puts `/root/mesos-ec2/copy-dir #{File.dirname(__FILE__)}` rescue 
  "~/mesos-ec2/copy-dir not found. Continuing without..."

if (ENV["SKIP_WARMUP"] != "1")
  # Warmup - write some data to Spark's tmp directory
  require "#{File.dirname(__FILE__) + "/config/config.rb"}"
  if JAVA_OPTS["spark.local.dir"]
    JAVA_OPTS["spark.local.dir"].split(",").map {|x| x.chomp}.each do |path|
      $stderr.puts "Warmup - Writing random data to #{path}."
      $stderr.puts `mkdir #{path}`
      $stderr.puts `dd if=/dev/random of=#{path}/random bs=#{1024*1024} count=4000`
      $stderr.puts `/root/mesos-ec2/copy-dir #{path}`
    end
  else
    $stderr.puts "Warning: spark.local.dir not set. Not performing disk warmup."
  end
end

# Run the tests
exec "#{File.dirname(__FILE__)}/script/all.rb"
