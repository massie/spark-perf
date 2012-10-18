#!/usr/bin/env ruby

require "#{File.dirname(__FILE__) + "/../config/config.rb"}"
require 'pathname'
require 'timeout'

# Set Spark Environment Variables
# --------------------------------------------------

ENV_VARS.each_pair do |k,v|
  ENV[k] = v unless v.nil?
end
ENV["SPARK_HOME"] = Pathname.new("spark").realpath.to_s

ENV["SPARK_JAVA_OPTS"] ||= ""
JAVA_OPTS.each_pair do |k,v|
  ENV["SPARK_JAVA_OPTS"] += "-D#{k}=#{v} "
end

# Adding spark-perf classes to the Spark classpath
ENV["SPARK_CLASSPATH"] = Pathname.new("#{File.dirname(__FILE__) +
  "/../target/scala-2.9.2/classes/"}").realpath.to_s

# Build the run command
# --------------------------------------------------

SPARK_DIR = (ENV["SPARK_HOME"] || (`pwd` + "/spark"))
cmd = "cd #{SPARK_DIR}; ./run #{ARGV.join(" ")}"
$stderr.puts cmd

# Execute the program with a timeout
begin
  @pipe = nil
  Timeout::timeout(TIMEOUT) do
    @pipe = IO.popen(cmd)
    Process.wait @pipe.pid
    puts @pipe.read
  end
rescue Timeout::Error
  puts "TIMEOUT"
  $stdout.flush
  # This is very ugly, but I haven't found a good way to handle this.
  # The Spark run scripts uses exec which assigns a different pid to the actual
  # java process. On most unix system the pid is just incremenet by one.
  Process.kill 9, @pipe.pid
  Process.kill 9, @pipe.pid + 1
end