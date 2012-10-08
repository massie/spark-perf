#!/usr/bin/env ruby

require "#{File.dirname(__FILE__) + "/../config/config.rb"}"

# Set Spark Environment Variables
ENV["SPARK_MEM"] = SPARK_MEM
ENV["SPARK_RDD_STORAGE_LEVEL"] = RDD_STORAGE_LEVEL

ENV["SPARK_JAVA_OPTS"] ||= ""
JAVA_OPTS.each_pair do |k,v|
  ENV["SPARK_JAVA_OPTS"] += "-D#{k}=#{v} "
end

ENV["MASTER"] = MASTER

# Build the run command
SPARK_DIR = (SPARK_HOME || ENV["SPARK_HOME"] || `pwd`)
cmd = "cd #{SPARK_DIR}; ./run #{ARGV.join(" ")}"
$stderr.puts cmd

# Execute the command
exec cmd