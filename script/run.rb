#!/usr/bin/env ruby

require "#{File.dirname(__FILE__) + "/../config/config.rb"}"
require 'pathname'

# Set Spark Environment Variables
# --------------------------------------------------

ENV["SPARK_HOME"] = Pathname.new("spark").realpath.to_s

ENV["SPARK_MEM"] = SPARK_MEM
ENV["SPARK_RDD_STORAGE_LEVEL"] = RDD_STORAGE_LEVEL

ENV["SPARK_JAVA_OPTS"] ||= ""
JAVA_OPTS.each_pair do |k,v|
  ENV["SPARK_JAVA_OPTS"] += "-D#{k}=#{v} "
end

ENV["MASTER"] = MASTER

# Adding spark-perf classes to the Spark classpath
ENV["SPARK_CLASSPATH"] = Pathname.new("#{File.dirname(__FILE__) +
  "/../target/scala-2.9.2/classes/"}").realpath.to_s

# Build the run command
# --------------------------------------------------

SPARK_DIR = (ENV["SPARK_HOME"] || (`pwd` + "/spark"))
cmd = "cd #{SPARK_DIR}; ./run #{ARGV.join(" ")}"
$stderr.puts cmd

# Execute the program
exec cmd